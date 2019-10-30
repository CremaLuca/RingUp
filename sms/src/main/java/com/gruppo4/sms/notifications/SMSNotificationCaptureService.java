package com.gruppo4.sms.notifications;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.gruppo4.sms.SMSPacket;

public class SMSNotificationCaptureService extends NotificationListenerService {

    /**
     * Called every time a notification pops up, cancels the notification if it is for the sms library
     *
     * @param sbn Notification coming from the status bar
     */
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String notificationText;
        Notification notification = sbn.getNotification();

        if (notification != null) {
            if (notification.category != null) {
                if (notification.category.equals(Notification.CATEGORY_MESSAGE)) { //if the notification is a message
                    Log.d("SMSNotifCaptureSVC", "Notification category is " + notification.category);
                    notificationText = notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString();
                    if (checkNotificationText(notificationText)) {
                        Log.d("SMSNotifCaptureSVC", "Notification canceled");
                        cancelNotification(sbn.getKey());
                    }
                }
            }
        }
    }

    /**
     * @return if the notification of category msg is suitable to be canceled
     */
    private boolean checkNotificationText(String notificationText) {
        String[] parts = notificationText.split(SMSPacket.SEPARATOR, 5);
        return parts.length >= 5;
    }
}
