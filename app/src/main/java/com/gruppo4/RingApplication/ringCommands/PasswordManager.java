package com.gruppo4.RingApplication.ringCommands;

import android.content.Context;

import com.gruppo_4.preferences.PreferencesManager;

/**
 * Class used to save and read the password from memory
 *
 * @author Alberto Ursino, Luca Crema
 */

public class PasswordManager {

    private static final String PREFERENCES_PASSWORD_KEY = "gruppo4_secret_password";

    /**
     * @param context  of the application
     * @param password password that want to be saved in memory
     */
    public static void setPassword(Context context, String password) {
        PreferencesManager.setString(context, PREFERENCES_PASSWORD_KEY, password);
    }

    /**
     * @param context of the application
     * @return the password saved in memory
     */
    public static String getPassword(Context context) {
        return PreferencesManager.getString(context, PREFERENCES_PASSWORD_KEY);
    }

    /**
     * Checks if there's a password saved in memory
     *
     * @param context of the application
     * @return true if there's a password saved in memory, false otherwise
     */
    public static boolean isPassSaved(Context context) {
        return !(PreferencesManager.getString(context, PREFERENCES_PASSWORD_KEY).equals(PreferencesManager.DEFAULT_STRING_RETURN));
    }

    /**
     * Deletes the saved password
     *
     * @param context of the application
     */
    public static void deletePassword(Context context){
        PreferencesManager.removeValue(context, PREFERENCES_PASSWORD_KEY);
    }
}
