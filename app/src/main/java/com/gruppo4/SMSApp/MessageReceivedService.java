package com.gruppo4.SMSApp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedServiceListener;


/**
 * @author Luca Crema, Alessandra Tonin, Marco Tommasini
 */
public class MessageReceivedService extends SMSReceivedServiceListener {

    public final static String STOP_ACTION = "stopAction";
    public final static String ALERT_ACTION = "alertAction";
    public final static String NOTIFICATION_ID = "notif_id";
    final static int TIME = 30 * 1000;

    private static Ringtone ringDef;

    @Override
    public void onMessageReceived(SMSMessage message) {

        createNotification();
        startAlarm();
    }

    /**
     * Creates a notification and sets a Intent for managing commands from there
     */
    private void createNotification() {

        final int notification_id = (int) System.currentTimeMillis();

        // StopAction stops the ringtone
        Intent stopIntent = new Intent(this, NotificationActionReceiver.class);
        stopIntent.setAction(STOP_ACTION);
        stopIntent.putExtra(NOTIFICATION_ID, notification_id);
        PendingIntent stopPI = PendingIntent.getBroadcast(this, notification_id, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // OpenAction opens the MainActivity
        // FLAG_ACTIVITY_SINGLE_TOP is used for having only one MainActivity running
        // otherwise the AlertDialog will not show up
        Intent openIntent = new Intent(this, MainActivity.class);
        openIntent.setAction(ALERT_ACTION);
        openIntent.putExtra(NOTIFICATION_ID, notification_id);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent openPI = PendingIntent.getActivity(this, notification_id, openIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Your phone is ringing")
                .setContentText("Stop it from here or open the app")
                .addAction(android.R.drawable.ic_lock_idle_alarm, "Stop", stopPI)
                .setContentIntent(openPI)
                .setAutoCancel(true);

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notification_id, builder.build());
        Log.d("MessageReceivedService","Notification created");

        //Cancel the notification after 30 seconds (as the ringtone stops playing)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                Log.d("MessageReceivedService","Ringtone stopped");
                notificationManager.cancel(notification_id);
            }
        }, TIME);

    }



    /**
     * Creates and starts the ringtone
     */
    private void startAlarm() {
        Context ctx = getApplicationContext();
        Ringtone ringtone = RingtoneManager.getRingtone(ctx, RingtoneManager.getDefaultUri( RingtoneManager.TYPE_RINGTONE));
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());

        if(ringDef == null)
            ringDef = ringtone;

        if (!ringDef.isPlaying())
            ringDef.play();

        //Stops the ringtone after 30 seconds (and cancels ringing notification) if the user doesn't stop it
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                Log.d("MessageReceivedService","Ringtone stopped");
                if(ringDef.isPlaying())
                    ringDef.stop();
            }
        }, TIME);
        Log.d("MessageReceivedService","Service started");
    }

    /**
     * Creates a Handler and stop the ringtone
     */
    public void stopAlarm()  {
        if(ringDef.isPlaying())
            ringDef.stop();
    }
}






































