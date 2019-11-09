package com.gruppo4.SMSApp.ringCommands;

import android.content.Context;
import android.util.Log;

import com.gruppo4.sms.dataLink.SMSManager;
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
    public static void onRingCommandReceived(Context context, RingCommand ringCommand) {
        if (RingHandler.checkPassword(context, ringCommand)) {
            //TODO RingtoneHandler.playRingtone(15);

        }
    }

    /**
     * @param ctx         of the application
     * @param ringCommand to send
     * @param listener
     */
    public static void sendCommand(Context ctx, RingCommand ringCommand, SMSSentListener listener) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        SMSMessage message = commandToMessage(ringCommand);
        SMSManager.getInstance(ctx).sendMessage(message, listener);
    }

    private static SMSMessage commandToMessage(RingCommand ringCommand) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        return new SMSMessage(1, ringCommand.getPeer(), "_"+ringCommand.getPassword());
    }
}
