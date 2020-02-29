package com.gruppo4.sms.dataLink.background;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.eis.smslibrary.SMSReceivedBroadcastReceiver;

/**
 * Android service that is kept alive and has the BroadcastReceiver attached so that it doesn't die on app closed
 *
 * @author Marco Tommasini
 * @author Luca Crema
 */
public class SMSKeepAliveService extends Service {

    private static SMSReceivedBroadcastReceiver broadcastReceiverInstance;

    /**
     * Callback method that allows clients to bind to this service class and interact with it
     *
     * @param arg0  the intent sent by clients that want to interact with this service
     * @return  IBinder object that defines the programming interface that clients can use to interact with the service
     */
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
        filter.setPriority(100);    //Set to a high value of priority, 100 is just a representative number
        registerReceiver(broadcastReceiverInstance, filter);
    }
}