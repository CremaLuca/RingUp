package com.gruppo4.sms.dataLink.listeners;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.gruppo4.communication.dataLink.listeners.ReceivedMessageListener;
import com.gruppo4.sms.dataLink.SMSMessage;

public abstract class SMSReceivedListener extends IntentService implements ReceivedMessageListener<SMSMessage> {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SMSReceivedListener(String name) {
        super(name);
    }

    public abstract void onMessageReceived(SMSMessage message);

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SMSMessage message = (SMSMessage) intent.getSerializableExtra("Message");
        onMessageReceived(message);
    }

}
