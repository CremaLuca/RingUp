package com.gruppo4.ringUp.structure;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gruppo4.ringUp.R;
import com.gruppo4.ringUp.activities.MainActivity;

/**
 * Class used to handle the ringUp notifications
 *
 * @author Alberto Ursino
 * @author Luca Crema
 */
public class NotificationHandler {

    public final static String STOP_RINGTONE_NOTIFICATION_ACTION = "stopRingtoneAction";
    public final static String PRESSED_NOTIFICATION_ACTION = "pressedNotificationAction";
    private static final String CLASS_TAG = "NotificationHandler";
    private final static int NOTIFICATION_ID = 666;

    /**
     * Creates a notification and sets a Intent for managing commands from there
     *
     * @param context current application's context.
     */
    public static void createNotification(@NonNull final Context context) {

        PendingIntent stopRingtonePI = getBroadcastStopRingtonePendingIntent(context);

        PendingIntent openAppPI = getBroadcastOpenAppPendingIntent(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.foreground_icon)
                .setContentTitle("Your phone is ringing") //TODO: TRANSLATE THIS
                .setContentText("Stop it from here or open the app") //TODO: TRANSLATE THAT
                .addAction(android.R.drawable.ic_lock_idle_alarm, "Stop", stopRingtonePI) // TODO: AND ALSO THIS
                .setContentIntent(openAppPI)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }

    private static PendingIntent getBroadcastStopRingtonePendingIntent(Context context) {
        //StopAction stops the defaultRing
        Intent stopIntent = new Intent(context, AppManager.class);
        stopIntent.setAction(STOP_RINGTONE_NOTIFICATION_ACTION);
        return PendingIntent.getBroadcast(context, NOTIFICATION_ID, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getBroadcastOpenAppPendingIntent(Context context) {
        //StopAction stops the defaultRing
        Intent openIntent = new Intent(context, MainActivity.class);
        openIntent.setAction(PRESSED_NOTIFICATION_ACTION);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, NOTIFICATION_ID, openIntent, 0);
    }

    /**
     * Removes a notification from the given notification ID (usually obtained from the intent after a press).
     *
     * @param context        current app context.
     */
    public static void removeRingNotification(Context context) {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID);
    }

}
