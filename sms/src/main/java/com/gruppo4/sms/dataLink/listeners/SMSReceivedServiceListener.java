package com.gruppo4.sms.dataLink.listeners;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSReceivedBroadcastReceiver;


/**
 * Abstract class to implement in order to wake up the service when a message is received
 *
 * @author Luca Crema, Alberto Ursino, Marco Tommasini, Alessandra Tonin
 * @since 29/11/2019
 */
public abstract class SMSReceivedServiceListener extends JobIntentService {

    /**
     * Callback for when a sms message sent from this library is received
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
    protected void onHandleWork(@Nullable Intent intent) {
        SMSMessage message = (SMSMessage) intent.getSerializableExtra(SMSReceivedBroadcastReceiver.INTENT_MESSAGE_TAG);
        onMessageReceived(message);
    }


}
