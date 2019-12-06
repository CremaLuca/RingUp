package com.gruppo4.sms.dataLink;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gruppo4.communication.dataLink.CommunicationHandler;
import com.gruppo4.sms.dataLink.listeners.SMSDeliveredListener;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedServiceListener;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;
import com.gruppo_4.preferences.PreferencesManager;

import java.lang.ref.WeakReference;


/**
 * Communication handler for SMSs. It's a Singleton, you should
 * access it with {@link #getInstance}, and before doing anything you
 * should call {@link #setup}.<br/>
 *
 * @author Luca Crema, Marco Mariotto, Alberto Ursino, Marco Tommasini, Marco Cognolato
 * @since 29/11/2019
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SMSHandler implements CommunicationHandler<SMSMessage> {

    public static final String SENT_MESSAGE_INTENT_ACTION = "SMS_SENT";
    public static final String DELIVERED_MESSAGE_INTENT_ACTION = "SMS_DELIVERED";
    public static final int RANDOM_STARTING_COUNTER_VALUE_RANGE = 100000;

    /**
     * Singleton instance
     */
    private static SMSHandler instance;

    /**
     * Weak reference doesn't prevent garbage collector to
     * de-allocate this class when it has reference to a
     * context that is still running. Prevents memory leaks.
     */
    private WeakReference<Context> context;

    /**
     * This message counter is used so that we can have a different action name
     * for pending intent (that will call broadcastReceiver). If we were to use the
     * same action name for every message we would have a conflict and we wouldn't
     * know what message has been sent
     */
    private int messageCounter;

    /**
     * Private constructor for Singleton
     */
    private SMSHandler() {
        //Random because if we close and open the app the value probably differs
        messageCounter = (int) (Math.random() * RANDOM_STARTING_COUNTER_VALUE_RANGE);
    }

    /**
     * @return the current instance of this class
     */
    public static SMSHandler getInstance() {
        if (instance == null)
            instance = new SMSHandler();
        return instance;
    }

    /**
     * Setup for the handler.
     *
     * @param context current context.
     */
    public void setup(Context context) {
        this.context = new WeakReference<>(context);
    }

    /**
     * Sends a message to a destination peer via SMS.
     * Requires {@link android.Manifest.permission#SEND_SMS}
     *
     * @param message to be sent in the channel to a peer
     */
    @Override
    public void sendMessage(final @NonNull SMSMessage message) {
        sendMessage(message, null, null);
    }

    /**
     * Sends a message to a destination peer via SMS then
     * calls the listener.
     *
     * @param message      to be sent in the channel to a peer
     * @param sentListener called on message sent or on error, can be null
     */
    public void sendMessage(final @NonNull SMSMessage message, final @Nullable SMSSentListener sentListener) {
        sendMessage(message, sentListener, null);
    }

    /**
     * Sends a message to a destination peer via SMS then
     * calls the listener.
     *
     * @param message           to be sent in the channel to a peer
     * @param deliveredListener called on message delivered or on error, can be null
     */
    public void sendMessage(final @NonNull SMSMessage message, final @Nullable SMSDeliveredListener deliveredListener) {
        sendMessage(message, null, deliveredListener);
    }

    /**
     * Sends a message to a destination peer via SMS then
     * calls the listener.
     *
     * @param message           to be sent in the channel to a peer
     * @param sentListener      called on message sent or on error, can be null
     * @param deliveredListener called on message delivered or on error, can be null
     */
    public void sendMessage(final @NonNull SMSMessage message,
                            final @Nullable SMSSentListener sentListener,
                            final @Nullable SMSDeliveredListener deliveredListener) {
        checkSetup();
        PendingIntent sentPI = setupNewSentReceiver(message, sentListener);
        PendingIntent deliveredPI = setupNewDeliverReceiver(message, deliveredListener);
        SMSCore.sendMessage(getSMSContent(message), message.getPeer().getAddress(), sentPI, deliveredPI);
    }

    /**
     * Creates a new {@link SMSSentBroadcastReceiver} and registers it to receive broadcasts
     * with action {@value SENT_MESSAGE_INTENT_ACTION}
     *
     * @param message  that will be sent
     * @param listener to call on broadcast received
     * @return a {@link PendingIntent} to be passed to SMSCore
     */
    private PendingIntent setupNewSentReceiver(final @NonNull SMSMessage message, final @Nullable SMSSentListener listener) {
        if (listener == null)
            return null; //Doesn't make any sense to have a BroadcastReceiver if there is no listener

        SMSSentBroadcastReceiver onSentReceiver = new SMSSentBroadcastReceiver(message, listener);
        String actionName = SENT_MESSAGE_INTENT_ACTION + (messageCounter++);
        context.get().registerReceiver(onSentReceiver, new IntentFilter(actionName));
        return PendingIntent.getBroadcast(context.get(), 0, new Intent(actionName), 0);
    }

    /**
     * Creates a new {@link SMSDeliveredBroadcastReceiver} and registers it to receive broadcasts
     * with action {@value DELIVERED_MESSAGE_INTENT_ACTION}
     *
     * @param message  that will be sent
     * @param listener to call on broadcast received
     * @return a {@link PendingIntent} to be passed to SMSCore
     */
    private PendingIntent setupNewDeliverReceiver(final @NonNull SMSMessage message, final @Nullable SMSDeliveredListener listener) {
        if (listener == null)
            return null; //Doesn't make any sense to have a BroadcastReceiver if there is no listener

        SMSDeliveredBroadcastReceiver onDeliveredReceiver = new SMSDeliveredBroadcastReceiver(message, listener);
        String actionName = DELIVERED_MESSAGE_INTENT_ACTION + (messageCounter++);
        context.get().registerReceiver(onDeliveredReceiver, new IntentFilter(actionName));
        return PendingIntent.getBroadcast(context.get(), 0, new Intent(actionName), 0);
    }

    /**
     * Checks if the handler has been setup
     *
     * @throws IllegalStateException if the handler has not been setup
     */
    private void checkSetup() {
        if (context == null)
            throw new IllegalStateException("You must call setup() first");
    }

    /**
     * Saves in memory the service class name to wake up. It doesn't need an
     * instance of the class, it just saves the name and instantiates it when needed.
     *
     * @param receivedListenerClassName the listener called on message received
     * @param <T>                       the class type that extends {@link SMSReceivedServiceListener} to be called
     */
    public <T extends SMSReceivedServiceListener> void setReceivedListener(Class<T> receivedListenerClassName) {
        checkSetup();
        PreferencesManager.setString(context.get(), SMSReceivedBroadcastReceiver.SERVICE_CLASS_PREFERENCES_KEY, receivedListenerClassName.toString());
    }

    /**
     * Unsubscribe the current {@link SMSReceivedServiceListener} from being called on message arrival
     */
    public void removeReceivedListener() {
        checkSetup();
        PreferencesManager.removeValue(context.get(), SMSReceivedBroadcastReceiver.SERVICE_CLASS_PREFERENCES_KEY);
    }

    /**
     * Helper function that gets the message content by using the pre-setup parser in {@link SMSMessageHandler}
     *
     * @param message to get the data from
     * @return the data parsed from the message
     */
    private String getSMSContent(SMSMessage message) {
        return SMSMessageHandler.getInstance().parseData(message);
    }

}
