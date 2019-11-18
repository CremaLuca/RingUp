package com.gruppo4.RingApplication.ringCommands;

import android.content.Context;

import com.gruppo_4.preferences.PreferencesManager;

/**
 * Class used to save and read the password from memory
 *
 * @author Alberto Ursino, Luca Crema
 * <p>
 * Code review for Bortoletti and Barca
 */

public class PasswordManager {

    private static final String PREFERENCES_PASSWORD_KEY = "gruppo4_secret_password";
    private static Context context;

    /**
     * Constructor that asks for context
     *
     * @param context of the application
     */
    public PasswordManager(Context context) {
        this.context = context;
    }

    /**
     * @param password password that want to be saved in memory
     */
    public static void setPassword(String password) {
        PreferencesManager.setString(context, PREFERENCES_PASSWORD_KEY, password);
    }

    /**
     * @return the password saved in memory
     */
    public static String getPassword() {
        return PreferencesManager.getString(context, PREFERENCES_PASSWORD_KEY);
    }

    /**
     * Checks if there's a password saved in memory
     *
     * @return true if there's a password saved in memory, false otherwise
     */
    public static boolean isPassSaved() {
        return !(PreferencesManager.getString(context, PREFERENCES_PASSWORD_KEY).equals(PreferencesManager.DEFAULT_STRING_RETURN));
    }

    /**
     * Deletes the saved password
     */
    public static void deletePassword() {
        PreferencesManager.removeValue(context, PREFERENCES_PASSWORD_KEY);
    }
}
