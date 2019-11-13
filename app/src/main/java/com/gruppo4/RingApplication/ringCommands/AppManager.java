package com.gruppo4.RingApplication.ringCommands;

import android.content.Context;
import android.media.Ringtone;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

/**
 * @author Alberto Ursino, Luca Crema, Alessandra Tonin, Marco Mariotto
 */
public class AppManager {

    private final static int TIME = 15 * 1000;

    /**
     * If the password of the message received is valid then play ringtone for fixed amount of time
     *
     * @param context     of the application
     * @param ringCommand a ring command not null
     */
    public static void onRingCommandReceived(Context context, RingCommand ringCommand, final Ringtone ringtone) {
        if (RingCommandHandler.checkPassword(context, ringCommand)) {
            Log.d("AppManager", "The password is correct");
            RingtoneHandler.playRingtone(ringtone);
            Log.d("AppManager", "Played ringtone");
            //Timer: the ringtone is playing for TIME seconds.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("AppManager", "Stopping ringtone");
                    RingtoneHandler.stopRingtone(ringtone);
                }
            }, TIME);
        } else {
            Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param context     of the application
     * @param ringCommand to send
     * @param listener
     */
    public static void sendCommand(Context context, RingCommand ringCommand, SMSSentListener listener) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        SMSMessage message = commandToMessage(context, ringCommand);
        SMSHandler.getInstance(context).sendMessage(message, listener);
    }

    private static SMSMessage commandToMessage(Context context, RingCommand ringCommand) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        return new SMSMessage(context, ringCommand.getPeer().toString(), ringCommand.getPassword());
    }
}
