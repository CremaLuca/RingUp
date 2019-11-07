package com.gruppo4.SMSApp;

import android.content.Context;
import com.gruppo_4.preferences.PreferencesManager;

/**
 * Class used to check if password in memory corresponds to the password passed through sender
 *
 * @author Alberto Ursino
 */

public class RingHandler {

    private static final String STRING_KEY = "String key";

    /**
     * Check of the password passed by RingCommand
     *
     * @param context     a valid context
     * @param ringCommand a valid RingCommand object
     * @return a boolean: true = passwords are corresponding, false = passwords are NOT corresponding
     */
    public static boolean checkPassword(Context context, RingCommand ringCommand) {
        return ringCommand.getPassword() == PreferencesManager.getString(context, STRING_KEY);
    }
}
