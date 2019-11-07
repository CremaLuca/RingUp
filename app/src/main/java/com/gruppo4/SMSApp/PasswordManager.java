package com.gruppo4.SMSApp;

import android.content.Context;

import com.gruppo_4.preferences.PreferencesManager;

/**
 * Class used to save and read the password from memory
 *
 * @author Alberto Ursino
 */

public class PasswordManager {

    private static PreferencesManager preferencesManager = new PreferencesManager();
    private static final String STRING_KEY = "gruppo4";

    /**
     *  Save the password in memory
     *
     * @param context a valid context
     * @param password password that want to be saved
     */
    public static void setPassword(Context context, String password){
        preferencesManager.setString(context, STRING_KEY, password);
    }

    /**
     * Returns the password saved in memory
     *
     * @param context valid context
     * @return the password
     */
    public static String getPassword(Context context) {
        return preferencesManager.getString(context, STRING_KEY);
    }
}
