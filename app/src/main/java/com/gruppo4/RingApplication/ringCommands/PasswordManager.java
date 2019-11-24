package com.gruppo4.RingApplication.ringCommands;

import android.content.Context;

import com.gruppo4.RingApplication.ringCommands.Interfaces.PasswordManagerInterface;
import it.lucacrema.preferences.PreferencesManager;

/**
 * Class used to save and read the password from memory
 *
 * @author Alberto Ursino, Luca Crema
 * <p>
 * Code reviewed by Bortoletti and Barca
 */

public class PasswordManager implements PasswordManagerInterface {

    private static final String PREFERENCES_PASSWORD_KEY = "group4_secret_password";
    private Context context;

    /**
     * Constructor: captures the context that will be used in the methods
     *
     * @param context of the application
     */
    public PasswordManager(Context context) {
        this.context = context;
    }

    @Override
    public String getPassword() {
        return PreferencesManager.getString(context, PREFERENCES_PASSWORD_KEY);
    }

    @Override
    public void setPassword(String password) {
        PreferencesManager.setString(context, PREFERENCES_PASSWORD_KEY, password);
    }

    @Override
    public boolean isPassSaved() {
        return !(PreferencesManager.getString(context, PREFERENCES_PASSWORD_KEY).equals(PreferencesManager.DEFAULT_STRING_RETURN));
    }

    @Override
    public void deletePassword() {
        PreferencesManager.removeValue(context, PREFERENCES_PASSWORD_KEY);
    }
}
