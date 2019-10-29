package com.gruppo4.sms;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class SMSNotificationCaptureService extends NotificationListenerService {

    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        cancelNotification(sbn.getKey());
    }

}
