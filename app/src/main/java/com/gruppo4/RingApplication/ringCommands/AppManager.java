package com.gruppo4.RingApplication.ringCommands;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.widget.Toast;

import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

/**
 * @author Alberto Ursino
 */
public class AppManager {

    /**
     * If the password of the message received is valid then play ringtone
     *
     * @param context     of the application
     * @param ringCommand a ring command not null
     */
    public static void onRingCommandReceived(Context context, RingCommand ringCommand, Ringtone ringtone) {
        if (RingCommandHandler.checkPassword(context, ringCommand)) {
            RingtoneHandler.playRingtone(ringtone);
        } else {
            Toast.makeText(context, "Password wrong", Toast.LENGTH_SHORT).show();
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
