package com.gruppo4.ringUp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.ringUp.R;
import com.gruppo4.ringUp.dialog.SetPasswordListener;
import com.gruppo4.ringUp.structure.PasswordManager;
import com.gruppo4.ringUp.dialog.PasswordDialog;

import static com.gruppo4.ringUp.activities.MainActivity.DIALOG_TAG;

/**
 * The following activity aims to inform the user about what the application is doing and to set the password of his device, necessary for using the app.
 *
 * @author Alberto Ursino
 * @version 1.0
 * @since 07/01/2020
 */
public class InstructionsActivity extends AppCompatActivity implements SetPasswordListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        Button setPassButton = findViewById(R.id.set_pass_button);
        setPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPassDialog();
            }
        });
    }

    /**
     * Method used to, in chronological order:
     * 1) Saves the personal device password in memory through the {@link PasswordManager} class
     * 2) Starts the {@link MainActivity}
     * 3) Closes the {@link InstructionsActivity}, it is no longer necessary
     *
     * @param password that will be stored
     * @param context  of the application
     * @author Alberto Ursino
     * @see SetPasswordListener#onPasswordSet(String, Context)
     */
    @Override
    public void onPasswordSet(String password, Context context) {
        PasswordManager.setPassword(context, password);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * If the user aborts the password setting, a toast is displayed telling him that the app needs it.
     *
     * @param context of the application
     * @author Alberto Ursino
     * @see SetPasswordListener#onPasswordNotSet(Context)
     */
    @Override
    public void onPasswordNotSet(Context context) {
        Toast.makeText(context, getString(R.string.toast_password_must_be_set), Toast.LENGTH_LONG).show();
    }

    /**
     * Creates the alert dialog through the {@link PasswordDialog} class with the {@link PasswordDialog#SET_PASS_COMMAND} command.
     *
     * @author Alberto Ursino
     */
    void openPassDialog() {
        PasswordDialog passwordDialog = new PasswordDialog(PasswordDialog.SET_PASS_COMMAND, getApplicationContext());
        passwordDialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }
}
