package com.gruppo4.sms.dataLink.background;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.eis.smslibrary.SMSReceivedBroadcastReceiver;

/**
 * Android service that is kept alive and has the BroadcastReceiver attached so that it doesn't die on app closed
 *
 * @author Marco Tommasini
 * @author Luca Crema
 */
public class SMSKeepAliveService extends Service {

    private static SMSReceivedBroadcastReceiver broadcastReceiverInstance;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * On startup it registers the broadcastReceiver
     */
    @Override
    public void onCreate() {
        registerMessageBroadcastReceiver();
    }

    /**
     * When the service is destroyed it un-registers the broadcastReceiver
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiverInstance);
        broadcastReceiverInstance = null;
    }

    /**
     * Registers the broadcastReceiver to this service
     */
    private void registerMessageBroadcastReceiver() {
        broadcastReceiverInstance = new SMSReceivedBroadcastReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(100);
        registerReceiver(broadcastReceiverInstance, filter);
    }
}