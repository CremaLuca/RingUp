package com.gruppo4.ringUp.dialog;

import android.content.Context;

/**
 * Listener used to manage when the {@link PasswordDialog#command} is {@link PasswordDialog#CHANGE_PASS_COMMAND}.
 *
 * @author Alberto Ursino
 * @version 1.0
 * @since 2019
 */
public interface ChangePasswordListener {
    /**
     * Method called when the user successfully changes the password in the dialog.
     * In other words when the user presses the positive button.
     *
     * @param password changed
     * @param context  of the application
     */
    void onPasswordChanged(String password, Context context);

    /**
     * Method called when the user aborts the password changing.
     * In other words when the user presses the negative button.
     *
     * @param context of the application
     */
    void onPasswordNotChanged(Context context);
}
