package com.gruppo4.sms.dataLink;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;

import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;
import com.gruppo_4.preferences.PreferencesManager;

import java.util.ArrayList;

public class SMSController {

    private static final String APPLICATION_CODE_PREFERENCES_KEY = "ApplicationCode";
    private static final String MESSAGE_SEQUENTIAL_CODE_PREFERENCES_KEY = "MessageSequentialCode";
    private static final String SENT_MESSAGE_INTENT_ACTION_PREFIX = "SENT_SMS";
    //list of listeners called when ALL packets of a message have been received
    private static ArrayList<SMSReceivedListener> receivedListeners = new ArrayList<>();
    private static ArrayList<SMSMessage> incompleteMessages = new ArrayList<>(); //these are partially constructed messages

    /**
     * Setup the controller, checks permissions and sets the application code
     *
     * @param context current app/service context
     * @param applicationCode an identifier for the current application
     */
    public static void setup(Context context, int applicationCode) {
        if (!checkApplicationCode(applicationCode))
            throw new IllegalStateException("Application code not valid, check it with checkApplicationCode() first");

        setApplicationCode(context, applicationCode);
    }

    /**
     * Send a SMSMessage, multiple packets could be sent
     * Requires Manifest.permission.SEND_SMS permission
     *
     * @param message  the message to be sent via SMS
     * @param listener called when the message is completely sent to the provider
     */
    public static void sendMessage(Context context, SMSMessage message, SMSSentListener listener) {
        ArrayList<String> messages = message.getPacketsContent();

        String intentAction = SENT_MESSAGE_INTENT_ACTION_PREFIX + "_" + message.getMessageId();
        ArrayList<PendingIntent> onSentIntents = setupPendingIntents(context, messages.size(), intentAction);

        //Setup broadcast receiver
        SMSSentBroadcastReceiver onSentReceiver = new SMSSentBroadcastReceiver(message, listener);
        context.registerReceiver(onSentReceiver, new IntentFilter(intentAction));

        //Check on packets
        for (String msg : messages) {
            Log.v("SMSController", "String to be sent, length: " + msg.length() + ", content: " + msg);
            ArrayList<String> dividedBySystem = SmsManager.getDefault().divideMessage(msg);
            if (dividedBySystem.size() > 1)
                throw new IllegalStateException("The message is too long (???)");
        }
        SMSCore.sendMessages(messages, message.getTelephoneNumber(), onSentIntents);
    }

    /**
     * Subscribes the listener to be called once a message is completely received
     * Requires Manifest.permission.RECEIVE_SMS permission
     *
     * @param listener a class that implements SMSReceivedListener
     */
    public static void addOnReceiveListener(SMSReceivedListener listener) {
        Log.v("SMSController", "addOnReceiveListener called for " + listener.getClass());
        receivedListeners.add(listener);
    }

    /**
     * Method used by SMSReceivedBroadcastReceiver to store a packet
     *
     * @param packet the sms content wrapped in a packet
     */
    public static void onReceive(SMSPacket packet, String telephoneNumber) {
        Log.v("SMSController", "Packet received, id:" + packet.getMessageId() + " number:" + packet.getPacketNumber() + " total:" + packet.getTotalNumber() + " from:" + telephoneNumber + " content:" + packet.getMessageText());
        //Let's see if we already have the message stored
        boolean found = false;
        for (SMSMessage m : incompleteMessages) {
            if (m.getTelephoneNumber().equals(telephoneNumber) && m.getMessageId() == packet.getMessageId()) {
                Log.v("SMSController", "Message for the id: " + packet.getMessageId() + " was already in the incomplete messages list");
                found = true;
                m.addPacket(packet);
                if (m.hasAllPackets()) {
                    Log.v("SMSController", "Message id:" + m.getMessageId() + " is now complete");
                    callOnReceivedListeners(m);
                    incompleteMessages.remove(m);
                }
                break;
            }
        }
        //If not found then create a new Message
        if (!found) {
            Log.v("SMSController", "Creating a new incomplete messages for id:" + packet.getMessageId());
            SMSMessage m = new SMSMessage(telephoneNumber, packet);
            incompleteMessages.add(m);
            if (m.hasAllPackets()) {
                Log.v("SMController", "The message id:" + m.getMessageId() + " is complete with one packet");
                callOnReceivedListeners(m);
                incompleteMessages.remove(m);
            }
        }
    }

    /**
     * Calls every listener subscribed for the reception of a message
     *
     * @param message a complete message
     */
    private static void callOnReceivedListeners(SMSMessage message) {
        Log.v("SMController", "Calling all listener for message " + message.getMessageId());
        for (SMSReceivedListener listener : receivedListeners) {
            listener.onSMSReceived(message);
        }
    }

    /**
     * Returns a sequential code for a new message to be sent
     *
     * @return a sequential code
     */
    static int getNewMessageId(Context ctx) {
        return PreferencesManager.shiftInt(ctx, MESSAGE_SEQUENTIAL_CODE_PREFERENCES_KEY, SMSMessage.MAX_ID);
    }

    /**
     * Creates an empty array list of pending intents except for the last one
     *
     * @param numberOfPackets
     * @return
     */
    private static ArrayList<PendingIntent> setupPendingIntents(Context context, int numberOfPackets, String intentAction) {
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(intentAction), 0);
        ArrayList<PendingIntent> onSentIntents = new ArrayList<>();
        for (int i = 0; i < numberOfPackets; i++) {
            onSentIntents.add(sentPI);
        }
        return onSentIntents;
    }

    /**
     * @param applicationCode a integer code.
     * @return true if the application code is valid.
     */
    public static boolean checkApplicationCode(int applicationCode) {
        return applicationCode > 0 && applicationCode < 1000;
    }

    /**
     * Writes in the memory the application code.
     *
     * @param ctx             current app or service context.
     * @param applicationCode a valid application code, checked with checkApplicationCode.
     */
    private static void setApplicationCode(Context ctx, int applicationCode) {
        PreferencesManager.setInt(ctx, APPLICATION_CODE_PREFERENCES_KEY, applicationCode);
    }

    /**
     * Gets the identifier application code from the memory.
     *
     * @param ctx current app or service context.
     * @return the current application code
     */
    public static int getApplicationCode(Context ctx) {
        int appCode = PreferencesManager.getInt(ctx, APPLICATION_CODE_PREFERENCES_KEY);
        if (appCode < 0)
            throw new IllegalStateException("Unable to perform the request, the SMS library has never been setup, call the setup() method at least once");
        return appCode;
    }

    /**
     * Checks if SEND_SMS permission is granted
     *
     * @param ctx a valid context
     * @return true if permission is granted
     */
    public static boolean checkSendPermission(Context ctx) {
        return ctx.checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Checks if RECEIVE_SMS permission is granted
     *
     * @param ctx a valid context
     * @return true if permission is granted
     */
    public static boolean checkReceivePermission(Context ctx) {
        return ctx.checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Checks if both SEND_SMS & RECEIVE_SMS permissions are granted
     *
     * @param ctx a valid context
     * @return true if both permissions are granted
     */
    public static boolean checkPermissions(Context ctx) {
        return checkSendPermission(ctx) && checkReceivePermission(ctx);
    }

    public enum SentState {
        MESSAGE_SENT,
        ERROR_GENERIC_FAILURE,
        ERROR_RADIO_OFF,
        ERROR_NULL_PDU,
        ERROR_NO_SERVICE,
        ERROR_LIMIT_EXCEEDED
    }
}
