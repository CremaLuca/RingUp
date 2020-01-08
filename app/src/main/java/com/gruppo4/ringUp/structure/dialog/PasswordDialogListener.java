package com.gruppo4.ringUp.structure.dialog;

import android.content.Context;

/**
 * Listener used to put in communication the main activity with the dialog
 */
public interface PasswordDialogListener {
    void onPasswordSet(Context context, String password);
}
