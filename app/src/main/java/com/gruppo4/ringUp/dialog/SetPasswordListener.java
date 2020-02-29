package com.gruppo4.ringUp.dialog;

import android.content.Context;

/**
 * Listener used to manage when the {@link PasswordDialog#command} is {@link PasswordDialog#SET_PASS_COMMAND}
 *
 * @author Alberto Ursino
 * @version 1.0
 * @since 2019
 */
public interface SetPasswordListener {
    /**
     * Method called when the user successfully enters a password in the dialog.
     * In other words when the user presses the positive button.
     *
     * @param password set
     * @param context  of the application
     */
    void onPasswordSet(String password, Context context);

    /**
     * Method called when the user aborts the password setting.
     * In other words when the user presses the negative button.
     *
     * @param context of the application
     */
    void onPasswordNotSet(Context context);
}
