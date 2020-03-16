package com.gruppo4.ringUp.structure;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gruppo4.ringUp.R;
import com.gruppo4.ringUp.activities.MainActivity;

/**
 * Class used to handle the ringUp notifications
 *
 * @author Alberto Ursino
 */
public class NotificationHandler extends BroadcastReceiver {

    public final static String STOP_ACTION = "stopAction";
    public final static String ALERT_ACTION = "alertAction";
    public final static String NOTIFICATION_ID_TAG = "notificationID";
    private final static int NOTIFICATION_ID_VALUE = 666;

    /**
     * Creates a notification and sets a Intent for managing commands from there
     *
     * @param context current application's context.
     */
    public static void createNotification(Context context) {

        final int notification_id = (int) System.currentTimeMillis();

        PendingIntent stopRingtonePI = getBroadcastStopRingtonePendingIntent(context);

        PendingIntent openAppPI = getBroadcastOpenAppPendingIntent(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.foreground_icon)
                .setContentTitle("Your phone is ringing") //TODO: TRANSLATE THIS
                .setContentText("Stop it from here or open the app") //TODO: TRANSLATE THAT
                .addAction(android.R.drawable.ic_lock_idle_alarm, "Stop", stopRingtonePI) // TODO: AND ALSO THIS
                .setContentIntent(openAppPI)
                .setAutoCancel(true);

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notification_id, builder.build());
        Log.d("MessageReceivedService", "Notification created");

        //Cancel the notification after the TIMEOUT_TIME seconds (as the defaultRing stops playing)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Log.d("MessageReceivedService", "Ringtone stopped");
            notificationManager.cancel(notification_id);
        }, AppManager.getInstance().timeoutTime);
    }

    private static PendingIntent getBroadcastStopRingtonePendingIntent(Context context) {
        //StopAction stops the defaultRing
        Intent stopIntent = new Intent(context, NotificationHandler.class);
        stopIntent.setAction(STOP_ACTION);
        stopIntent.putExtra(NOTIFICATION_ID_TAG, NOTIFICATION_ID_VALUE);
        return PendingIntent.getBroadcast(context, NOTIFICATION_ID_VALUE, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getBroadcastOpenAppPendingIntent(Context context) {
        //StopAction stops the defaultRing
        Intent openIntent = new Intent(context, MainActivity.class);
        openIntent.setAction(ALERT_ACTION);
        openIntent.putExtra(NOTIFICATION_ID_TAG, NOTIFICATION_ID_VALUE);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, NOTIFICATION_ID_VALUE, openIntent, 0);
    }

    /**
     * Removes a notification from the given notification ID (usually obtained from the intent after a press).
     *
     * @param context        current app context.
     * @param notificationID id of the notification to remove.
     */
    public static void removeNotifications(Context context, int notificationID) {
        NotificationManagerCompat.from(context).cancel(notificationID);
    }

    /**
     * Manages the received intents from the broadcast.
     * Should handle the STOP_ACTION intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;
        if (!STOP_ACTION.equals(intent.getAction()))
            return;

        int notificationID = intent.getIntExtra(NOTIFICATION_ID_TAG, -1);
        AppManager.getInstance().onNotificationPressed(context, notificationID);
    }

}
