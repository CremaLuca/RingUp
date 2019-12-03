package com.gruppo4.RingApplication.structure;

import android.content.Context;
import android.media.Ringtone;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.eis.smslibrary.SMSHandler;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.eis.smslibrary.listeners.SMSSentListener;
import com.gruppo4.RingApplication.structure.exceptions.WrongPasswordException;

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
     * @param ringtone    to be played
     * @throws WrongPasswordException Exception thrown when the password received is not valid
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onRingCommandReceived(Context context, @NonNull RingCommand ringCommand, final Ringtone ringtone) throws WrongPasswordException {
        final RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();

        if (checkPassword(context, ringCommand)) {
            ringtoneHandler.playRingtone(ringtone);
            //Timer: the ringtone is playing for TIMEOUT_TIME seconds.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ringtoneHandler.stopRingtone(ringtone);
                }
            }, TIMEOUT_TIME);
        } else {
            throw new WrongPasswordException();
        }
    }

    /**
     * Method used to send a RingCommand via SMS using the library class {@link SMSHandler}
     *
     * @param context         of the application
     * @param ringCommand     to send
     * @param smsSentListener Listener used to inform that the message has been sent
     * @throws InvalidSMSMessageException      could be launched by the RingCommandHandler method "parseCommand"
     * @throws InvalidTelephoneNumberException could be launched by the RingCommandHandler method "parseCommand"
     */
    public void sendCommand(Context context, @NonNull RingCommand ringCommand, SMSSentListener smsSentListener) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        SMSHandler smsHandler = SMSHandler.getInstance();
        smsHandler.setup(context);
        smsHandler.sendMessage(RingCommandHandler.getInstance().parseCommand(ringCommand), smsSentListener);
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
