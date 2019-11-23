package com.gruppo4.RingApplication.ringCommands;

import android.content.Context;
import android.media.Ringtone;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.gruppo4.RingApplication.MainActivity;
import com.gruppo4.RingApplication.ringCommands.Interfaces.AppManagerInterface;
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
public class AppManager implements AppManagerInterface {

    private final static String WRONG_PASSWORD = "Wrong Password";
    private RingCommandHandler ringCommandHandler = RingCommandHandler.getInstance();
    private RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();

    private static AppManager appManager = new AppManager();

    /**
     * Private constructor
     */
    private AppManager() {
    }

    /**
     * @return the singleton
     */
    public static AppManager getInstance() {
        return appManager;
    }

    @Override
    public void onRingCommandReceived(Context context, RingCommand ringCommand, final Ringtone ringtone) {
        if (ringCommandHandler.checkPassword(context, ringCommand)) {
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

    @Override
    public void sendCommand(Context context, RingCommand ringCommand, SMSSentListener listener) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        SMSMessage message = commandToMessage(context, ringCommand);
        SMSHandler.getInstance(context).sendMessage(message, listener);
    }

    /**
     * @param context     of the application
     * @param ringCommand to transform
     * @return an SMSMessage object created from the ring command
     * @throws InvalidTelephoneNumberException
     * @throws InvalidSMSMessageException
     */
    private SMSMessage commandToMessage(Context context, RingCommand ringCommand) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        return new SMSMessage(context, ringCommand.getPeer().toString(), ringCommand.getPassword());
    }
}
