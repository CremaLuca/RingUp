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
 * Class used to inform the user on what ringUp is and what it is used for.
 * It also requires the setting up of the device password.
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
     * 1) Saves the password in memory through the {@link PasswordManager} class
     * 2) Starts the {@link MainActivity}
     * 3) Closes the {@link InstructionsActivity}, it is no longer necessary
     *
     * @param password that will be stored
     * @param context  of the application
     * @author Alberto Ursino
     */
    @Override
    public void onPasswordSet(String password, Context context) {
        PasswordManager.setPassword(context, password);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * If the user doesn't set any password, a toast is displayed telling him that the app needs it
     * For more information: {@link SetPasswordListener#onPasswordSet(String, Context)}
     *
     * @param context of the application
     * @author Alberto Ursino
     */
    @Override
    public void onPasswordNotSet(Context context) {
        Toast.makeText(context, getString(R.string.toast_password_must_be_set), Toast.LENGTH_LONG).show();
    }

    /**
     * Creates the dialog used to insert a non empty password or exit/abort
     * For more information: {@link SetPasswordListener#onPasswordNotSet(Context)}
     *
     * @author Alberto Ursino
     */
    void openPassDialog() {
        PasswordDialog passwordDialog;
        passwordDialog = new PasswordDialog(PasswordDialog.SET_PASS_COMMAND, getApplicationContext());
        passwordDialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }
}
