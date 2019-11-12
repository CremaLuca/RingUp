package com.gruppo4.RingApplication.ringCommands;

import android.content.Context;
import com.gruppo_4.preferences.PreferencesManager;

/**
 * Class used to save and read the password from memory
 *
 * @author Alberto Ursino, Luca Crema
 */

public class PasswordManager {

    private static final String STRING_KEY = "gruppo4_secret_password";

    /**
     * @param context  of the application
     * @param password password that want to be saved in memory
     */
    public static void setPassword(Context context, String password) {
        PreferencesManager.setString(context, STRING_KEY, password);
    }

    /**
     * @param context of the application
     * @return the password saved in memory
     */
    public static String getPassword(Context context) {
        return PreferencesManager.getString(context, STRING_KEY);
    }

    /**
     * Checks if there's a password saved in memory
     * true = yes, false = not saved
     *
     * @param context of the application
     */
    public static boolean isPassSaved(Context context) {
        return !(PreferencesManager.getString(context, STRING_KEY) == null);
    }
}
