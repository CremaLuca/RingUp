package com.gruppo4.sms.dataLink;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

class SMSKillerService extends Service {

    private static SMSReceivedBroadcastReceiver m_ScreenOffReceiver;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        registerScreenOffReceiver();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(m_ScreenOffReceiver);
        m_ScreenOffReceiver = null;
    }

    private void registerScreenOffReceiver() {
        m_ScreenOffReceiver = new SMSReceivedBroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("SMSBackground", "ACTION_SCREEN_OFF");
                // do something, e.g. send Intent to main app
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(m_ScreenOffReceiver, filter);
    }
}