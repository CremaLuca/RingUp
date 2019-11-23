package com.gruppo4.RingApplication.ringCommands.dialog;

import android.content.Context;

/**
 * Listener used to put in communication the main activity with the dialog
 */
public interface PasswordDialogListener {

    void onPasswordSet(String password, Context context);

}
