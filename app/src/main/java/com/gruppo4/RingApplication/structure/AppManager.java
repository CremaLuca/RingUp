package com.gruppo4.RingApplication.structure;

import android.content.Context;
import android.media.Ringtone;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.eis.smslibrary.listeners.SMSSentListener;
import com.gruppo4.RingApplication.structure.exceptions.IllegalPasswordException;

/**
 * This is a singleton class used to manage a received RingCommand or to send one
 *
 * @author Alberto Ursino, Luca Crema, Alessandra Tonin, Marco Mariotto
 */
public class AppManager {

    private static final int TIMEOUT_TIME = 30 * 1000; //30 seconds

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
     * If the password of the RingCommand received is valid then play ringtone for fixed amount of time
     *
     * @param context     of the application
     * @param ringCommand received
     * @param ringtone    A valid ringtone to be played
     * @throws IllegalPasswordException Exception thrown when the password received is not valid
     */
    public void onRingCommandReceived(Context context, @NonNull RingCommand ringCommand, final Ringtone ringtone) throws IllegalPasswordException {
        //Instantiation of the RingtoneHandler singleton class, will be used below
        final RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();

        //Controls if the password in the RingCommand object corresponds with the one in memory, if not then launches an exception
        if (!(checkPassword(context, ringCommand)))
            throw new IllegalPasswordException();

        //Exception weren't thrown so let's play the ringtone!
        ringtoneHandler.playRingtone(ringtone);
        //Timer: the ringtone is playing for TIMEOUT_TIME seconds.
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ringtoneHandler.stopRingtone(ringtone);
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

