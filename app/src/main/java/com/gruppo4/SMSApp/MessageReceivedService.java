package com.gruppo4.SMSApp;

import android.content.Context;
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
 * @author Luca Crema, Alessandra Tonin
 */
public class MessageReceivedService extends SMSReceivedListenerService {

    private Ringtone ringDef;

    public MessageReceivedService() {
        super("MessageReceivedService");
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

        //try to make phone ring from service
        Context ctx = getApplicationContext();
        Ringtone ringtone = RingtoneManager.getRingtone(ctx, RingtoneManager.getDefaultUri( RingtoneManager.TYPE_RINGTONE));
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
        ringDef = ringtone;
        ringDef.play();
        Log.d("MSGRecSVC","Service started and timeout about to start");
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                Log.d("MSGRecSVC","ringdef:" + ringDef);
                ringDef.stop();
            }
        }, 10*1000);

    }

}






































