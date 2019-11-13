package com.gruppo4.SMSApp;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
