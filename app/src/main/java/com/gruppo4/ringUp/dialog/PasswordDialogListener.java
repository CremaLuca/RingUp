package com.gruppo4.ringUp.dialog;

import android.content.Context;

/**
 * Listener used to manage the actions taken in the PasswordDialog
 *
 * @author Alberto Ursino
 */
public interface PasswordDialogListener {
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
