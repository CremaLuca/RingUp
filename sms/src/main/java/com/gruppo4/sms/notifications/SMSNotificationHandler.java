package com.gruppo4.sms.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.core.app.NotificationManagerCompat;

import java.util.Set;

public class SMSNotificationHandler {

    /**
     * If the app doesn't have notification listening permissions, it opens the system page/activity for them to be granted
     *
     * @param currentActivity current open activity
     */
    public static void automaticNotificationListenerSetup(Activity currentActivity) {
        if (!isNotificationListenerEnabled(currentActivity.getApplicationContext())) {
            openNotificationListenerSettings(currentActivity);
        }
    }

    /**
     * @param context the current context
     * @return true if permission to listen to notification is already granted
     */
    public static boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        return packageNames.contains(context.getPackageName());
    }

    /**
     * Opens the system page/activity to give permissions to listen to notifications
     *
     * @param activity the current activity
     */
    public static void openNotificationListenerSettings(Activity activity) {
        try {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
