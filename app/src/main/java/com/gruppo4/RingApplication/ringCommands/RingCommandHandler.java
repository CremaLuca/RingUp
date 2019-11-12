package com.gruppo4.RingApplication.ringCommands;

import android.content.Context;
import com.gruppo4.sms.dataLink.SMSPeer;

/**
 * Class used to check if password in memory corresponds to the password passed through sender
 *
 * @author Alberto Ursino, Luca Crema, Marco Mariotto
 */

public class RingCommandHandler {

    public static final String SPLIT_CHARACTER = "_";

    /**
     * Extracts the password from the message received and create a RingCommand, null if the command is not valid
     *
     * @param peer    is the sender/receiver of the message
     * @param content command
     * @return a RingCommand
     */
    protected static RingCommand parseContent(SMSPeer peer, String content) {
        if ((content.charAt(0) + "").equals(SPLIT_CHARACTER)) {
            String[] parts = content.split(SPLIT_CHARACTER);
            //parts[0] is empty, parts[1] contains the password
            return new RingCommand(peer, parts[1]);
        }
        //Message received is not a command for play the ringtone
        return null;
    }

    /**
     * Verify that the password in the RingCommand is the same as the one in memory
     *
     * @param context     a valid context
     * @param ringCommand a valid RingCommand object
     * @return a boolean: true = passwords are corresponding, false = passwords are NOT corresponding
     */
    public static boolean checkPassword(Context context, RingCommand ringCommand) {
        return ringCommand.getPassword().equals(PasswordManager.getPassword(context));
    }
}
