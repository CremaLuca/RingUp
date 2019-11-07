package com.gruppo4.SMSApp;

import android.content.Context;

/**
 * Class used to check if password in memory corresponds to the password passed through sender
 *
 * @author Alberto Ursino
 *
 * revised by Luca Crema
 */

public class RingHandler {

    private static final String STRING_KEY = "gruppo4_secret_password";

    /**
     * The method extracts the password from the message received
     */
    private static RingCommand parseString(String content) {
        //...
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
