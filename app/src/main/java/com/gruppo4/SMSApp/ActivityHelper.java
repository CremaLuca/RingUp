package com.gruppo4.SMSApp;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;

public class ActivityHelper extends SMSReceivedListener {

    public ActivityHelper() {
        super("ActivityHelper");
    }

    @Override
    public void onMessageReceived(SMSMessage message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "123")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Test notification")
                .setContentText("This is a test notification Luca set")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1423, builder.build());
    }

}
