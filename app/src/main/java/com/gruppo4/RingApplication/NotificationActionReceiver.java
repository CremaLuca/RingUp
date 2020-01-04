package com.gruppo4.RingApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import com.gruppo4.RingApplication.structure.AppManager;

/**
 * Manages the result of an action triggered by a notification
 *
 * @author Marco Tommasini
 * @author Implemented by Alberto Ursino
 */
public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            switch (intent.getAction()) {
                case AppManager.STOP_ACTION: {
                    AppManager.getInstance().stopRingtone();
                    int id = intent.getIntExtra(AppManager.NOTIFICATION_ID, -1);
                    notificationManager.cancel(id);
                    AppManager.notificationFlag--;
                    break;
                }
                default:
                    break;
            }
        }
    }

}
