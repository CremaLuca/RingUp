package com.gruppo4.RingApplication.structure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

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
            AppManager appManager = AppManager.getInstance();

            switch (intent.getAction()) {
                case AppManager.STOP_ACTION: {
                    appManager.stopRingtone();
                    int id = intent.getIntExtra(appManager.NOTIFICATION_ID, -1);
                    notificationManager.cancel(id);
                    break;
                }
                default:
                    break;
            }
        }
    }

}
