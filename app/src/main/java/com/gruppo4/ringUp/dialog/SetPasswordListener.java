package com.gruppo4.ringUp.dialog;

import android.content.Context;

/**
 * Listener used to manage when the {@link PasswordDialog#command} is {@link PasswordDialog#SET_PASS_COMMAND}
 *
 * @author Alberto Ursino
 * @version 1.0
 * @since 24/02/2020
 */
public interface SetPasswordListener {
    /**
     * Method called when the user set a password
     *
     * @param password set
     * @param context  of the application
     */
    void onPasswordSet(String password, Context context);

    /**
     * Method called when the user aborts the password setting
     *
     * @param context of the application
     */
    void onPasswordNotSet(Context context);
}
