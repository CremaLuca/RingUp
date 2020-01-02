package com.gruppo4.RingApplication.structure;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.eis.smslibrary.listeners.SMSSentListener;
import com.gruppo4.RingApplication.MainActivity;
import com.gruppo4.RingApplication.R;
import com.gruppo4.RingApplication.structure.exceptions.IllegalPasswordException;

/**
 * This is a singleton class used to manage a received RingCommand or to send one
 *
 * @author Alberto Ursino, Luca Crema, Alessandra Tonin, Marco Mariotto
 */
public class AppManager {

    private static final int TIMEOUT_TIME = 30 * 1000; //30 seconds
    public final static String STOP_ACTION = "stopAction";
    public final static String ALERT_ACTION = "alertAction";
    public final static String NOTIFICATION_ID = "notificationID";
    private static Ringtone defaultRing;

    /**
     * Instance of the class that is instantiated in getInstance method
     */
    private static AppManager instance = null;

    /**
     * Private constructor
     */
    private AppManager() {
    }

    /**
     * @return the AppManager instance
     */
    public static AppManager getInstance() {
        if (instance == null)
            instance = new AppManager();
        return instance;
    }

    /**
     * If the password of the RingCommand received is valid then play defaultRing for fixed amount of time
     *
     * @param context     of the application
     * @param ringCommand received
     * @param ringtone    A valid defaultRing to be played
     * @throws IllegalPasswordException Exception thrown when the password received is not valid
     */
    public void onRingCommandReceived(Context context, @NonNull RingCommand ringCommand, final Ringtone ringtone) throws IllegalPasswordException {
        defaultRing = ringtone;

        //Controls if the password in the RingCommand object corresponds with the one in memory, if not then launches an exception
        if (!(checkPassword(context, ringCommand)))
            throw new IllegalPasswordException();

        //Exception weren't thrown so let's play the defaultRing!
        RingtoneHandler.getInstance().playRingtone(defaultRing);
        //Timer: the defaultRing is playing for TIMEOUT_TIME seconds.
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRingtone();
            }
        }, TIMEOUT_TIME);

        createNotification(context);
    }

    /**
     * Stops the defaultRing
     */
    public void stopRingtone() {
        if (defaultRing.isPlaying())
            RingtoneHandler.getInstance().stopRingtone(defaultRing);
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
    private void createNotification(Context context) {

        final int notification_id = (int) System.currentTimeMillis();

        //StopAction stops the defaultRing
        Intent stopIntent = new Intent(context, NotificationActionReceiver.class);
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
                .setSmallIcon(R.drawable.first_icon_foreground)
                .setContentTitle("Your phone is ringing")
                .setContentText("Stop it from here or open the app")
                .addAction(android.R.drawable.ic_lock_idle_alarm, "Stop", stopPI)
                .setContentIntent(openPI)
                .setAutoCancel(true);

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notification_id, builder.build());
        Log.d("MessageReceivedService", "Notification created");

        //Cancel the notification after 30 seconds (as the defaultRing stops playing)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("MessageReceivedService", "Ringtone stopped");
                notificationManager.cancel(notification_id);
            }
        }, TIMEOUT_TIME);

    }

    /**
     * Method used to send a RingCommand via SMS using the library class {@link SMSManager}
     *
     * @param context         of the application
     * @param ringCommand     to send
     * @param smsSentListener Listener used to inform that the message has been sent
     * @throws InvalidSMSMessageException      could be launched by the RingCommandHandler method "parseCommand"
     * @throws InvalidTelephoneNumberException could be launched by the RingCommandHandler method "parseCommand"
     */
    public void sendCommand(Context context, @NonNull RingCommand ringCommand, SMSSentListener smsSentListener) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        SMSManager.getInstance().sendMessage(RingCommandHandler.getInstance().parseCommand(ringCommand), smsSentListener, context);

    }

    /**
     * Checks if the RingCommand password and the one saved in memory corresponds
     *
     * @param context     a valid context
     * @param ringCommand a valid RingCommand object
     * @return a boolean: true if passwords corresponds, false otherwise
     */
    private boolean checkPassword(Context context, @NonNull RingCommand ringCommand) {
        return ringCommand.getPassword().equals(new PasswordManager(context).getPassword());
    }

}

