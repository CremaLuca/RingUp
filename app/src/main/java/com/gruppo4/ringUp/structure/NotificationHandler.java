package com.gruppo4.ringUp.structure;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

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
 * @since 16/03/2020
 */
public class NotificationHandler {

    public final static String STOP_RINGTONE_NOTIFICATION_ACTION = "stopRingtoneAction";
    public final static String PRESSED_NOTIFICATION_ACTION = "pressedNotificationAction";
    private final static int NOTIFICATION_ID = 666;
    public static final String CHANNEL_NAME = "RingUp Ringtone Channel";
    public static final String CHANNEL_ID = "666";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Stop RingUp ringtones";

    /**
     * Creates a notification and sets the intents to manage callbacks.
     *
     * @param context current application's context.
     */
    public static void createNotification(@NonNull final Context context) {

        PendingIntent stopRingtonePI = getBroadcastStopRingtonePendingIntent(context);

        PendingIntent openAppPI = getBroadcastOpenAppPendingIntent(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.foreground_icon)
                .setContentTitle("Your phone is ringing") //TODO: TRANSLATE THIS
                .setContentText("Stop it from here or open the app") //TODO: TRANSLATE THAT
                .addAction(android.R.drawable.ic_lock_idle_alarm, "Stop", stopRingtonePI) // TODO: AND ALSO THIS
                .setContentIntent(openAppPI)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * Must be called before creating other notifications. Sets up the android notification channel, needed for API > 25.
     *
     * @param context current application's context.
     */
    public static void createNotificationChannel(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //IMPORTANCE_HIGH makes pop-up the notification
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            } else {
                Log.d("NotificationHandler", "getSystemService(NotificationManager.class), in createNotificationChannel method, returns a null object");
            }
        }
    }

    /**
     * Creates the PendingIntent to be thrown when the notification button "stop" is pressed
     *
     * @param context current application's context
     * @return A Pending intent to stop the ringtone without opening the app.
     */
    private static PendingIntent getBroadcastStopRingtonePendingIntent(Context context) {
        Intent stopIntent = new Intent(context, AppManager.class);
        stopIntent.setAction(STOP_RINGTONE_NOTIFICATION_ACTION);
        return PendingIntent.getBroadcast(context, NOTIFICATION_ID, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Creates the PendingIntent to be thrown when the notification is "opened" by pressing on it
     *
     * @param context current application's context
     * @return A Pending intent to open the MainActivity.
     */
    private static PendingIntent getBroadcastOpenAppPendingIntent(@NonNull final Context context) {
        Intent openIntent = new Intent(context, MainActivity.class);
        openIntent.setAction(PRESSED_NOTIFICATION_ACTION);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, NOTIFICATION_ID, openIntent, 0);
    }

    /**
     * Removes a notification from the given notification ID (usually obtained from the intent after a press).
     *
     * @param context current app context.
     */
    public static void removeRingNotification(Context context) {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID);
    }

}
