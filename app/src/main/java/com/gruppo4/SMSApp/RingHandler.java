package com.gruppo4.SMSApp;

import android.content.Context;
import com.gruppo_4.preferences.PreferencesManager;

public class RingHandler {

    private static final String STRING_KEY = "String key";

    /**
     * Check of the password passed by RingCommand
     *
     * @param context     a valid context
     * @param ringCommand a valid RingCommand object
     * @return a boolean: true = passwords are corresponding, false = passwords are corresponding
     */
    public static boolean checkPassword(Context context, RingCommand ringCommand) {
        return ringCommand.getPassword() == PreferencesManager.getString(context, STRING_KEY);
    }
}
