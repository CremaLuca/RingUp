package com.gruppo4.sms;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class SMSNotificationCaptureService extends NotificationListenerService {

    //Cancel notifications from our application's messages
    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        String text = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT).toString();
        Log.d("1","++++++++++++++++++++++++++++++++"+text);
        String subText = "";
        if(text!=null)
            subText = text.substring(0,4);
        Log.d("1","++++++++++++++++++++++++++++++++"+subText);
        if(subText.equals("123_")) {
            cancelNotification(sbn.getKey());
        }
    }

}
