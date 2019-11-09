package com.gruppo4.SMSApp.ringCommands;

import android.content.Context;

import com.gruppo4.sms.dataLink.SMSPeer;

/**
 * Class used to check if password in memory corresponds to the password passed through sender
 *
 * @author Alberto Ursino
 * revised by Luca Crema
 */

public class RingHandler {

    private static final String INVISIBLE_CHARACTER = (char) 0x02 + "";  //Invisible character

    /**
     * Extracts the password from the message received and create a RingCommand, null if the message has the wrong format
     *
     * @param peer    is the sender/receiver of the message
     * @param content of the message
     * @return a RingCommand
     */
    public static RingCommand parseContent(SMSPeer peer, String content) {
        int numInvChar = 0;
        //Control if the content has 2 invisible character
        for (int i = 0; i < content.length(); i++) {
            if ((content.charAt(i) + "").equals(INVISIBLE_CHARACTER))
                numInvChar++;
        }
        //If the content has 2 invisible character then can build a valid ringCommand object
        if (numInvChar == 2) {
            String[] parts = content.split(INVISIBLE_CHARACTER);
            //parts[0] is empty, parts[1] contains a command, parts[2] contains the password
            return new RingCommand(peer, parts[2]);
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
