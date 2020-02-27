package com.gruppo4.SMSApp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.listeners.SMSReceivedServiceListener;


/**
 * Service called when receives a SMSMessage from the broadcast receiver
 * Than it creates the notification and starts the alarm
 *
 * @author Marco Tommasini
 * @author Alessandra Tonin
 */
public class MessageReceivedService extends SMSReceivedServiceListener {

    final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

    public final static String STOP_ACTION = "stopAction";
    public final static String ALERT_ACTION = "alertAction";
    public final static String NOTIFICATION_ID = "notif_id";
    final static int TIME = 30 * 1000; //30 seconds

    private static Ringtone ringDef;

    /**
     * Called when a message is received from the broadcast receiver
     *
     * @param message the SMSMessage received, built and parsed by the broadcast receiver
     */
    @Override
    public void onMessageReceived(SMSMessage message) {

        final int notification_id = createNotification();
        startAlarm();

        //Stops the ringtone after 30 seconds and cancels ringing notification if the user doesn't stop it
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                notificationManager.cancel(notification_id);
                stopAlarm();
            }
        }, TIME);
    }

    /**
     * Creates a notification and sets a Intent for managing commands from there
     * Two actions available in the notification represented by two intents:
     * Stop --> stops the ringtone
     * Open --> opens the MainActivity
     *
     * @return  id of the notification just created
     */
    private int createNotification() {

        //Creating unique id
        int notif_id = (int) System.currentTimeMillis();

        //StopAction (button)
        Intent stopIntent = new Intent(this, NotificationActionReceiver.class);
        stopIntent.setAction(STOP_ACTION);
        stopIntent.putExtra(NOTIFICATION_ID, notif_id);
        PendingIntent stopPI = PendingIntent.getBroadcast(this, notif_id, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // OpenAction (touching the notification)
        // FLAG_ACTIVITY_SINGLE_TOP is used for having only one MainActivity running
        // otherwise the AlertDialog will not show up
        Intent openIntent = new Intent(this, MainActivity.class);
        openIntent.setAction(ALERT_ACTION);
        openIntent.putExtra(NOTIFICATION_ID, notif_id);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent openPI = PendingIntent.getActivity(this, notif_id, openIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getResources().getString(R.string.notification_title))
                .setContentText(getResources().getString(R.string.notification_text))
                .addAction(android.R.drawable.ic_lock_idle_alarm, getResources().getString(R.string.stop), stopPI)
                .setContentIntent(openPI)
                .setAutoCancel(true);

        notificationManager.notify(notif_id, builder.build());

        return notif_id;
    }

    /**
     * Creates and starts the ringtone
     * Only one ringtone at a time
     */
    private void startAlarm() {
        Context ctx = getApplicationContext();
        Ringtone ringtone = RingtoneManager.getRingtone(ctx, RingtoneManager.getDefaultUri( RingtoneManager.TYPE_RINGTONE));
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());

        if(ringDef == null)
            ringDef = ringtone;

        if (!ringDef.isPlaying())
            ringDef.play();
    }

    /**
     * Stops the ringtone
     */
    public void stopAlarm()  {
        if(ringDef.isPlaying())
            ringDef.stop();
    }
}






































