package com.gruppo4.SMSApp;

import android.content.Context;
import com.gruppo_4.preferences.PreferencesManager;

/**
 * Class used to save and read the password from memory
 *
 * @author Alberto Ursino
 */

public class PasswordManager {

    private static final String STRING_KEY = "gruppo4_secret_password";

    /**
     * @param context  a valid context
     * @param password password that want to be saved in memory
     */
    public static void setPassword(Context context, String password) {
        PreferencesManager.setString(context, STRING_KEY, password);
    }

    /**
     * @param context a valid context
     * @return the password saved in memory
     */
    public static String getPassword(Context context) {
        return PreferencesManager.getString(context, STRING_KEY);
    }
}
