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
import com.gruppo4.ringUp.structure.exceptions.IllegalPasswordException;
import com.gruppo4.ringUp.structure.ringtone.AudioUtilityManager;
import com.gruppo4.ringUp.structure.ringtone.RingtoneHandler;

import static com.gruppo4.ringUp.structure.NotificationHandler.notificationFlag;

/**
 * This is a singleton class used to manage a received RingCommand or to send one
 *
 * @author Alberto Ursino
 * @author Luca Crema
 * @author Alessandra Tonin
 * @author Marco Mariotto
 */
public class AppManager {

    public int timeoutTime = 20000;

    /**
     * Instance of the class that is instantiated in getInstance method
     */
    private static AppManager instance = null;

    /**
     * @return the AppManager instance
     */
    public static AppManager getInstance() {
        if (instance == null)
            instance = new AppManager();
        return instance;
    }

    /**
     * Private constructor
     */
    private AppManager() {
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
    public void onRingCommandReceived(@NonNull final Context context, @NonNull final RingCommand ringCommand, @NonNull final Ringtone ringtone) throws IllegalPasswordException {

        //Controls if the password in the RingCommand object corresponds with the one in memory, if not then launches an exception
        if (!(checkPassword(context, ringCommand)))
            throw new IllegalPasswordException();

        //Exception wasn't thrown so play the ringtone at full volume.
        AudioUtilityManager.setMaxVolume(context, AudioUtilityManager.ALARM);
        RingtoneHandler.getInstance().playRingtone(ringtone);

        //I no notification is pending
        //TODO: refactor this thing, how come there are notifications "pending"?
        if (!notificationFlag)
            NotificationHandler.createNotification(context);

        //Timer: the defaultRing is playing for timeoutTime seconds.
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            RingtoneHandler.getInstance().stopRingtone(ringtone);
        }, timeoutTime);
    }

    /**
     * Method used to send a ring command via SMS using the library class {@link SMSManager}.
     * The ring command is obtained through {@link RingCommandHandler#parseCommand(RingCommand)} which transforms a {@link RingCommand}
     * in a {@link com.eis.smslibrary.SMSMessage} with the right {@link RingCommandHandler#SIGNATURE}.
     *
     * @param context         of the application
     * @param ringCommand     to send
     * @param smsSentListener Listener used to inform that the message has been sent
     * @throws InvalidSMSMessageException      could be launched by {@link RingCommandHandler#parseCommand(RingCommand)}
     * @throws InvalidTelephoneNumberException could be launched by {@link RingCommandHandler#parseCommand(RingCommand)}
     * @author Alberto Ursino
     * @author Luca Crema
     */
    public void sendCommand(@NonNull final Context context, @NonNull final RingCommand ringCommand, @NonNull final SMSSentListener smsSentListener) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
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
        return ringCommand.getPassword().equals(PasswordManager.getPassword(context));
    }

}

