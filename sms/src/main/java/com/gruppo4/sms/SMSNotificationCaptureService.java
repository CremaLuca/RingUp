package com.gruppo4.sms;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class SMSNotificationCaptureService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String notificationText;
        String subText = "";
        Notification notification = sbn.getNotification();

        if (notification != null) {
            if (notification.category != null) {
                if (notification.category.equals(Notification.CATEGORY_MESSAGE)) { //if the notification is a message
                    Log.d("SMSNotifCaptureSVC", "Notification category is " + notification.category);
                    notificationText = notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString();
                    String[] parts = notificationText.split(SMSPacket.SEPARATOR, 5);
                    if (parts.length >= 5) {
                        Log.d("SMSNotifCaptureSVC", "Notification canceled");
                        cancelNotification(sbn.getKey());
                    }
                }
            }
        }
    }
}
