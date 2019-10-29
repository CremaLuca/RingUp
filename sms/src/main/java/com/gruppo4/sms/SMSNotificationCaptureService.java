package com.gruppo4.sms;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class SMSNotificationCaptureService extends NotificationListenerService {


    //Cancel notifications from our application's messages
    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        String subText = sbn.getNotification().extras.getString("android.text").substring(0,4);
        if(subText.equals("123_"))
            cancelNotification(sbn.getKey());
    }

}
