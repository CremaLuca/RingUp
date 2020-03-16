package com.gruppo4.ringUp.structure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.eis.smslibrary.listeners.SMSSentListener;
import com.gruppo4.ringUp.structure.exceptions.IllegalPasswordException;
import com.gruppo4.ringUp.structure.ringtone.AudioUtilityManager;
import com.gruppo4.ringUp.structure.ringtone.RingtoneHandler;

/**
 * This is a singleton class used to manage a received RingCommand or to send one
 *
 * @author Alberto Ursino
 * @author Luca Crema
 */
public class AppManager extends BroadcastReceiver {

    /**
     * Instance of the class that is instantiated in getInstance method
     */
    private static AppManager instance = null;
    public int timeoutTime = 20000;

    /**
     * Private constructor.
     */
    private AppManager() {
    }

    /**
     * Broadcast receiver's method to handle received intents.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;
        switch (intent.getAction()) {
            case NotificationHandler.STOP_RINGTONE_NOTIFICATION_ACTION:
                onNotificationStopButton(context);
                break;
        }
    }

    /**
     * @return the AppManager instance.
     */
    public static AppManager getInstance() {
        if (instance == null)
            instance = new AppManager();
        return instance;
    }

    /**
     * Sets the app ready to work properly.
     *
     * @param context current app context.
     */
    public void setup(@NonNull final Context context) {
        //Retrieve preferred timeout time
        timeoutTime = retrievePreferencesTimeoutTime();
    }

    /**
     * If the password of the RingCommand received is valid then play defaultRing for fixed amount of time
     *
     * @param context     of the application
     * @param ringCommand received
     * @param ringtone    A valid defaultRing to be played
     * @throws IllegalPasswordException Exception thrown when the password received is not correct. FIXME: WHAT? Nah fuck this, just don't do anywhing
     */
    public void onRingCommandReceived(@NonNull final Context context, @NonNull final RingCommand ringCommand, @NonNull final Ringtone ringtone) throws IllegalPasswordException {

        //Controls if the password in the RingCommand object corresponds with the one in memory, if not then launches an exception
        if (!(checkPassword(context, ringCommand)))
            throw new IllegalPasswordException();

        startRingtone(context, ringtone);

        NotificationHandler.createNotification(context);

        setTimer(context, timeoutTime);
    }

    /**
     * Method used to send a ring command via SMS using the library class {@link SMSManager}.
     * The ring command is obtained through {@link RingCommandHandler#parseCommand(RingCommand)} which transforms a {@link RingCommand}
     * in a {@link com.eis.smslibrary.SMSMessage} with the right {@link RingCommandHandler#SIGNATURE}.
     *
     * @param context         of the application.
     * @param ringCommand     to send.
     * @param smsSentListener Listener used to inform that the message has been sent.
     * @throws InvalidSMSMessageException      could be launched by {@link RingCommandHandler#parseCommand(RingCommand)}.
     * @throws InvalidTelephoneNumberException could be launched by {@link RingCommandHandler#parseCommand(RingCommand)}.
     */
    public void sendRingCommand(@NonNull final Context context, @NonNull final RingCommand ringCommand, @Nullable final SMSSentListener smsSentListener) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        SMSManager.getInstance().sendMessage(RingCommandHandler.getInstance().parseCommand(ringCommand), smsSentListener, context);
    }

    /**
     * Checks if the RingCommand password and the one saved in memory are the same.
     *
     * @param context     a valid context.
     * @param ringCommand a valid RingCommand object.
     * @return a boolean: true if passwords corresponds, false otherwise.
     */
    private boolean checkPassword(@NonNull final Context context, @NonNull final RingCommand ringCommand) {
        return ringCommand.getPassword().equals(PasswordManager.getPassword(context));
    }

    /**
     * Retrieves timeout time from the preferences.
     *
     * @return timeout time in ms.
     */
    private int retrievePreferencesTimeoutTime() {
        //TODO: changeable timeout time
        return 20000;
    }

    /**
     * Sets max volume and plays a ringtone.
     *
     * @param context  current app context.
     * @param ringtone ringtone to play.
     */
    private void startRingtone(@NonNull final Context context, @NonNull final Ringtone ringtone) {
        AudioUtilityManager.setMaxVolume(context, AudioUtilityManager.ALARM);
        RingtoneHandler.getInstance().playRingtone(ringtone);
    }

    /**
     * Stops all ringtone from playing and removes notification.
     *
     * @param context current app context.
     */
    public void stopRingtone(@NonNull final Context context) {
        RingtoneHandler.getInstance().stopAllRingtones();
        NotificationHandler.removeRingNotification(context);
    }

    /**
     * Sets a timer to go off in {@code timerTime} milliseconds. Then stops all ringtones.
     *
     * @param context   current app context.
     * @param timerTime time in ms for the timer.
     */
    private void setTimer(@NonNull final Context context, int timerTime) {
        //Timer: the defaultRing is playing for timeoutTime seconds.
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            stopRingtone(context);
        }, timerTime);
    }

    /**
     * Callback for when the notification ("the phone is ringing") is pressed.
     */
    public void onNotificationStopButton(@NonNull final Context context) {
        stopRingtone(context);
        NotificationHandler.removeRingNotification(context);
    }

}

