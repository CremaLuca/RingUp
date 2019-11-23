package com.gruppo4.sms.dataLink;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class SMSKillerService extends Service {

    private static SMSReceivedBroadcastReceiver m_ScreenOffReceiver;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.v("SMSKillerSVC", "Service onCreate");
        registerScreenOffReceiver();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(m_ScreenOffReceiver);
        m_ScreenOffReceiver = null;
        Log.v("SMSKillerSVC", "Service destroyed");
    }

    private void registerScreenOffReceiver() {
        m_ScreenOffReceiver = new SMSReceivedBroadcastReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(100);
        registerReceiver(m_ScreenOffReceiver, filter);
        Log.v("SMSKillerSVC", "Service registered the receiver");
    }
}