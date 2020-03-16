package com.gruppo4.ringUp.structure;

import android.content.Context;

import it.lucacrema.preferences.PreferencesManager;

/**
 * Controls a String in memory.
 *
 * @author Alberto Ursino
 */
public class PasswordManager {

    /**
     * Through this key we can access the memory location where we store the password
     */
    static final String PREFERENCES_PASSWORD_KEY = "secret_password";

    /**
     * @return the password saved in memory
     */
    public static String getPassword(Context context) {
        return PreferencesManager.getString(context, PREFERENCES_PASSWORD_KEY);
    }

    /**
     * @param password that want to be saved in memory
     */
    public static void setPassword(Context context, String password) {
        PreferencesManager.setString(context, PREFERENCES_PASSWORD_KEY, password);
    }

    /**
     * Checks if there's a password saved in memory
     *
     * @return true if is it present, false otherwise
     */
    public static boolean isPassSaved(Context context) {
        return !(PreferencesManager.getString(context, PREFERENCES_PASSWORD_KEY).equals(PreferencesManager.DEFAULT_STRING_RETURN));
    }

    /**
     * Deletes the password saved in memory
     */
    public static void deletePassword(Context context) {
        PreferencesManager.removeValue(context, PREFERENCES_PASSWORD_KEY);
    }


}
