package com.gruppo4.sms;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class SMSNotificationCaptureService extends NotificationListenerService {

    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        String text = "";
        String subText = "";

        if(sbn.getNotification()!=null) {
            try {
                text = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT).toString();
                Log.d("1","++++++++++++++++++++++++++++++++"+text);
                subText = text.substring(0,4);
            } catch (NullPointerException e) { }
        }

        Log.d("1","++++++++++++++++++++++++++++++++"+subText);

        //Cancel the notification
        if(subText.equals("123_")) {
            cancelNotification(sbn.getKey());
        }
    }
}
