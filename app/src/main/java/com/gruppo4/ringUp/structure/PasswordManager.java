package com.gruppo4.ringUp.structure;

import android.content.Context;

import it.lucacrema.preferences.PreferencesManager;

/**
 * Class used to control the saving of a password
 *
 * @author Alberto Ursino
 */
public class PasswordManager {

    /**
     * Through this key we can access the memory location where we store the password
     */
    private static final String PREFERENCES_PASSWORD_KEY = "secret_password";

    private Context context;

    /**
     * Constructor captures the context that will then be used in the methods of this class
     *
     * @param context of the application
     */
    public PasswordManager(Context context) {
        this.context = context;
    }

    /**
     * @return the password saved in memory
     */
    public String getPassword() {
        return PreferencesManager.getString(context, PREFERENCES_PASSWORD_KEY);
    }

    /**
     * @param password that want to be saved in memory
     */
    public void setPassword(String password) {
        PreferencesManager.setString(context, PREFERENCES_PASSWORD_KEY, password);
    }

    /**
     * Checks if there's a password saved in memory
     *
     * @return true if is it present, false otherwise
     */
    public boolean isPassSaved() {
        return !(PreferencesManager.getString(context, PREFERENCES_PASSWORD_KEY).equals(PreferencesManager.DEFAULT_STRING_RETURN));
    }

    /**
     * Deletes the password saved in memory
     */
    public void deletePassword() {
        PreferencesManager.removeValue(context, PREFERENCES_PASSWORD_KEY);
    }


}
