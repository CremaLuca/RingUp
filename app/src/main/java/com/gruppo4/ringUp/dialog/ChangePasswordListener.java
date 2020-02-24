package com.gruppo4.ringUp.dialog;

import android.content.Context;

/**
 * Listener used to manage when the {@link PasswordDialog#command} is {@link PasswordDialog#CHANGE_PASS_COMMAND}
 *
 * @author Alberto Ursino
 * @version 1.0
 * @since 24/02/2020
 */
public interface ChangePasswordListener {
    /**
     * Method called when the user changed the password
     *
     * @param password changed
     * @param context  of the application
     */
    void onPasswordChanged(String password, Context context);

    /**
     * Method called when the user aborts the password changing
     *
     * @param context of the application
     */
    void onPasswordNotChanged(Context context);
}
