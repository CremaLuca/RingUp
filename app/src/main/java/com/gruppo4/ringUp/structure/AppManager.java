package com.gruppo4.ringUp.structure;

import android.content.Context;
import android.media.Ringtone;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.eis.smslibrary.listeners.SMSSentListener;
import com.gruppo4.ringUp.structure.audioUtility.AudioUtilityManager;
import com.gruppo4.ringUp.structure.exceptions.IllegalPasswordException;

import static com.gruppo4.ringUp.structure.NotificationHandler.notificationFlag;

/**
 * This is a singleton class used to manage a received RingCommand or to send one
 *
 * @author Alberto Ursino, Luca Crema, Alessandra Tonin, Marco Mariotto
 */
public class AppManager {

    public static final int TIMEOUT_TIME = 20 * 1000;
    public static Ringtone defaultRing;

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
     * @author Alberto Ursino
     */
    public void onRingCommandReceived(Context context, @NonNull RingCommand ringCommand, final Ringtone ringtone) throws IllegalPasswordException {
        defaultRing = ringtone;

        //Controls if the password in the RingCommand object corresponds with the one in memory, if not then launches an exception
        if (!(checkPassword(context, ringCommand)))
            throw new IllegalPasswordException();

        //Exception weren't thrown so let's play the defaultRing!
        RingtoneHandler.getInstance().playRingtone(defaultRing);
        AudioUtilityManager.setMinVolume(context, AudioUtilityManager.ALARM);

        if (!notificationFlag)
            NotificationHandler.createNotification(context);

        //Timer: the defaultRing is playing for TIMEOUT_TIME seconds.
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            RingtoneHandler.getInstance().stopRingtone(defaultRing);
        }, TIMEOUT_TIME);


    }

    /**
     * Method used to send a RingCommand via SMS using the library class {@link SMSManager}
     *
     * @param context         of the application
     * @param ringCommand     to send
     * @param smsSentListener Listener used to inform that the message has been sent
     * @throws InvalidSMSMessageException      could be launched by {@link RingCommandHandler#parseCommand(RingCommand)}
     * @throws InvalidTelephoneNumberException could be launched by {@link RingCommandHandler#parseCommand(RingCommand)}
     * @author Alberto Ursino
     * @author Luca Crema
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
     * @author Alberto Ursino
     * @author Luca Crema
     */
    private boolean checkPassword(Context context, @NonNull RingCommand ringCommand) {
        return ringCommand.getPassword().equals(new PasswordManager(context).getPassword());
    }

}

