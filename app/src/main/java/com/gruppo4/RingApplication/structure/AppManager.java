package com.gruppo4.RingApplication.structure;

import android.content.Context;
import android.media.Ringtone;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.gruppo4.RingApplication.MainActivity;
import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;
import com.gruppo_4.preferences.PreferencesManager;

import static java.lang.Integer.parseInt;

/**
 * @author Alberto Ursino, Luca Crema, Alessandra Tonin, Marco Mariotto
 */
public class AppManager {

    private final static String WRONG_PASSWORD = "Wrong Password";

    /**
     * Instance of the class that is instantiated in getInstance method
     */
    private static AppManager instance = null;

    //RingtoneHandler is a singleton
    private static RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();

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
     * If the password of the message received is valid then play ringtone for fixed amount of time
     *
     * @param context     of the application
     * @param ringCommand a ring command not null
     */
    public void onRingCommandReceived(Context context, RingCommand ringCommand, final Ringtone ringtone) {
        if (checkPassword(context, ringCommand)) {
            ringtoneHandler.playRingtone(ringtone);
            Log.d("Timer value saved: ", "" + PreferencesManager.getInt(context, MainActivity.TIMEOUT_TIME_PREFERENCES_KEY));
            //Timer: the ringtone is playing for TIME seconds.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ringtoneHandler.stopRingtone(ringtone);
                }
            }, PreferencesManager.getInt(context, MainActivity.TIMEOUT_TIME_PREFERENCES_KEY));
        } else {
            Toast.makeText(context, WRONG_PASSWORD, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Calls the class SMS Handler and passed to it an SMS Message object, built with the ring command
     *
     * @param context     of the application
     * @param ringCommand to send
     * @param listener
     * @throws InvalidTelephoneNumberException
     * @throws InvalidSMSMessageException
     */
    public void sendCommand(Context context, RingCommand ringCommand, SMSSentListener listener) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        SMSMessage message = commandToMessage(context, ringCommand);
        SMSHandler.getInstance(context).sendMessage(message, listener);
    }

    /**
     * @param context     of the application
     * @param ringCommand to transform
     * @return an SMSMessage object created from the ring command
     */
    private SMSMessage commandToMessage(Context context, RingCommand ringCommand) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        return new SMSMessage(context, ringCommand.getPeer().toString(), ringCommand.getPassword());
    }

    /**
     * Verify that the password in the RingCommand is the same as the one in memory
     *
     * @param context     a valid context
     * @param ringCommand a valid RingCommand object
     * @return a boolean: true = passwords are corresponding, false = passwords are NOT corresponding
     */
    public boolean checkPassword(Context context, RingCommand ringCommand) {
        return ringCommand.getPassword().equals(new PasswordManager(context).getPassword());
    }

}
