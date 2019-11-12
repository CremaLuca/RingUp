package com.gruppo4.SMSApp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;

public class ActivityHelper implements SMSReceivedListener {

    @Override
    public void onMessageReceived(SMSMessage message) {

    }

    @Override
    public void onMessageReceived(SMSMessage message, Context ctx) {
        Log.d("ActivityHelper", "Messaggio ricevuto da " + message.getPeer().getAddress());
        Toast.makeText(ctx, "Messaggio ricevuto da " + message.getPeer().getAddress(), Toast.LENGTH_SHORT).show();
    }
}
