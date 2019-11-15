package com.gruppo4.SMSApp;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * @author Tommasini Marco
 */

public class ActivityHelperService extends Service {

    ActivityHelperReceiver activityHelperReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Create IntentFilter and register the ActivityHelperReceiver with app open or closed
     */
    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SMSApp");
        intentFilter.setPriority(100);

        activityHelperReceiver = new ActivityHelperReceiver();

        registerReceiver(activityHelperReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(activityHelperReceiver != null) {
            unregisterReceiver(activityHelperReceiver);
        }
    }
}
