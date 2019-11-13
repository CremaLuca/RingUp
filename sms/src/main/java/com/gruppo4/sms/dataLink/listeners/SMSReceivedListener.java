package com.gruppo4.sms.dataLink.listeners;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.gruppo4.communication.dataLink.listeners.ReceivedMessageListener;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSReceivedBroadcastReceiver;

/**
 * Abstract class to implement in order to wake up the service when a message is received
 * Service MUST be added to app manifest
 *
 * @author Luca Crema
 */
public abstract class SMSReceivedListener extends IntentService implements ReceivedMessageListener<SMSMessage> {

    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SMSReceivedListener(String name) {
        super(name);
    }

    /**
     * Callback for when a sms message is received
     *
     * @param message the received message
     */
    public abstract void onMessageReceived(SMSMessage message);

    /**
     * Handles the service call, extracts the message from the intent extras and calls {@link #onMessageReceived(SMSMessage)}
     *
     * @param intent intent passed from the broadcastReceiver
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SMSMessage message = (SMSMessage) intent.getSerializableExtra(SMSReceivedBroadcastReceiver.INTENT_MESSAGE_NAME);
        onMessageReceived(message);
    }

}
