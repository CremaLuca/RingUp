package com.gruppo4.SMSApp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedListenerService;


/**
 * @author Luca Crema, Alessandra Tonin, Marco Tommasini
 */
public class MessageReceivedService extends SMSReceivedListenerService {

    private final static String STOP_ACTION = "stopAction";
    private Ringtone ringDef;

    private NotificationActionReceiver notificationReceiver;

    public MessageReceivedService() {
        super("MessageReceivedService");
    }

    @Override
    public void onMessageReceived(SMSMessage message) {
        notificationReceiver = new NotificationActionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(STOP_ACTION);
        registerReceiver(notificationReceiver, filter);

        createNotification();

        startAlarm();
    }

    /**
     * Creatse a notification and sets a Intent for managing commands from there
     */
    private void createNotification() {

        // Stop Action that stops the ringtone
        Intent stopIntent = new Intent(this, NotificationActionReceiver.class);
        stopIntent.setAction(STOP_ACTION);
        PendingIntent stopPI = PendingIntent.getBroadcast(this, 0, stopIntent, 0);

        // Open Action that opens the MainActivity
        Intent openIntent = new Intent(this, MainActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent openPI = PendingIntent.getActivity(this, 0, openIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "123")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Your phone is ringing")
                .setContentText("Stop it from here or open the app")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(android.R.drawable.ic_lock_idle_alarm, "Stop", stopPI)
                .setContentIntent(openPI)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1423, builder.build());
    }

    /**
     * Creates and starts the ringtone
     */
    private void startAlarm() {
        Context ctx = getApplicationContext();
        Ringtone ringtone = RingtoneManager.getRingtone(ctx, RingtoneManager.getDefaultUri( RingtoneManager.TYPE_RINGTONE));
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());

        ringDef = ringtone;
        ringDef.play();

        Log.d("MSGRecSVC","Service started");
    }

    /**
     * Creates a Handler and stop the ringtone
     */
    private void stopAlarm()  {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                Log.d("MSGRecSVC","ringdef:" + ringDef);
                if(ringDef.isPlaying())
                    ringDef.stop();
            }
        }, 0);
    }

    public class NotificationActionReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                switch (intent.getAction()) {

                    case STOP_ACTION: {
                        stopAlarm();
                        break;
                    }
                    default:
                        break;
                }
                unregisterReceiver(this);
            }
        }
    }
}






































