package com.gruppo4.RingApplication.ringCommands.dialog;

import android.content.Context;

/**
 * listener used to put in communication the main activity with the dialog
 */
public interface PasswordDialogListener {
    void applyText(String password, Context context);
}
