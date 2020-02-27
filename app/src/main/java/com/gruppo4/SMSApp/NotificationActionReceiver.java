package com.gruppo4.SMSApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;


/**
 * Manages the result of an action triggered by a notification
 *
 * @author Marco Tommasini
 */
public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            MessageReceivedService service = new MessageReceivedService();

            if(intent.getAction() != null) {
                switch (intent.getAction()) {

                    //Stops the alarm and cancels the specific notification which launched this action
                    case MessageReceivedService.STOP_ACTION: {
                        service.stopAlarm();
                        int id = intent.getIntExtra(MessageReceivedService.NOTIFICATION_ID, -1);
                        notificationManager.cancel(id);
                        break;
                    }

                    default:
                        break;
                }
            }
        }
    }
}
