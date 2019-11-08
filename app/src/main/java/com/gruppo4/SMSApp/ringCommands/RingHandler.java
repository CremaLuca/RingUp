package com.gruppo4.SMSApp.ringCommands;

import android.content.Context;

import com.gruppo4.SMSApp.PasswordManager;
import com.gruppo4.SMSApp.ringCommands.RingCommand;
import com.gruppo4.sms.dataLink.SMSPeer;

/**
 * Class used to check if password in memory corresponds to the password passed through sender
 *
 * @author Alberto Ursino
 * revised by Luca Crema
 */

public class RingHandler {

    private static final String SEPARATOR_CHARACTER = (char) 0x02 + "";  //Invisible character

    /**
     * The method extracts the password from the message received and create a RingCommand
     *
     * @param peer    is the sender/receiver of the message
     * @param content of the message, it MUST be like: "text message"+SEPARATOR_CHARACTER"+password"
     * @return a RingCommand with the peer and the password
     */
    protected static RingCommand parseString(SMSPeer peer, String content) {
        String[] parts = content.split(SEPARATOR_CHARACTER);
        String password = parts[1];
        return new RingCommand(peer, password);
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
