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

import com.gruppo4.ringUp.MainActivity;
import com.gruppo4.ringUp.R;

/**
 * Class used to handle the ringUp notifications
 */
public class NotificationHandler extends BroadcastReceiver {

    public final static String STOP_ACTION = "stopAction";
    public final static String ALERT_ACTION = "alertAction";
    public final static String NOTIFICATION_ID = "notificationID";
    /**
     * flag used to avoid creation of more than one notification
     * false -> no notification is pending, true -> a notification is already pending
     */
    public static boolean notificationFlag = false;

    /**
     * Manages the intent received
     *
     * @author Implemented by Alberto Ursino
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            switch (intent.getAction()) {
                case STOP_ACTION: {
                    RingtoneHandler.getInstance().stopRingtone(AppManager.defaultRing);
                    int id = intent.getIntExtra(NOTIFICATION_ID, -1);
                    notificationManager.cancel(id);
                    notificationFlag = false;
                    break;
                }
                default:
                    Log.d("NotificationHandler", "onReceive -> action is null");
                    break;
            }
        } else {
            Log.d("NotificationHandler", "onReceive -> intent is null");
        }
    }

    /**
     * Creates a notification and sets a Intent for managing commands from there
     *
     * @param context of the application
     * @author Marco Tommasini
     * @author Luca Crema
     * @author Alessandra Tonin
     * @author Implemented by Alberto Ursino
     */
    public static void createNotification(Context context) {
        notificationFlag = true;

        final int notification_id = (int) System.currentTimeMillis();

        //StopAction stops the defaultRing
        Intent stopIntent = new Intent(context, NotificationHandler.class);
        stopIntent.setAction(STOP_ACTION);
        stopIntent.putExtra(NOTIFICATION_ID, notification_id);
        PendingIntent stopPI = PendingIntent.getBroadcast(context, notification_id, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //OpenAction opens the MainActivity
        //LAG_ACTIVITY_SINGLE_TOP is used for having only one MainActivity running
        //otherwise the AlertDialog will not show up
        Intent openIntent = new Intent(context, MainActivity.class);
        openIntent.setAction(ALERT_ACTION);
        openIntent.putExtra(NOTIFICATION_ID, notification_id);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent openPI = PendingIntent.getActivity(context, notification_id, openIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.foreground_icon)
                .setContentTitle("Your phone is ringing")
                .setContentText("Stop it from here or open the app")
                .addAction(android.R.drawable.ic_lock_idle_alarm, "Stop", stopPI)
                .setContentIntent(openPI)
                .setAutoCancel(true);

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notification_id, builder.build());
        Log.d("MessageReceivedService", "Notification created");

        //Cancel the notification after the TIMEOUT_TIME seconds (as the defaultRing stops playing)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Log.d("MessageReceivedService", "Ringtone stopped");
            notificationManager.cancel(notification_id);
            notificationFlag = false;
        }, AppManager.TIMEOUT_TIME);
    }

}
