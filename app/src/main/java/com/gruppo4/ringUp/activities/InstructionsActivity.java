package com.gruppo4.ringUp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.ringUp.R;
import com.gruppo4.ringUp.structure.PasswordManager;
import com.gruppo4.ringUp.dialog.PasswordDialog;
import com.gruppo4.ringUp.dialog.PasswordDialogListener;
import com.gruppo4.ringUp.structure.exceptions.IllegalCommandException;

import static com.gruppo4.ringUp.activities.MainActivity.DIALOG_TAG;
import static com.gruppo4.ringUp.activities.MainActivity.SET_PASS_COMMAND;

/**
 * Class used to inform the user on what ringUp is and what it is used for.
 * It also requires the setting up of the device password.
 *
 * @author Alberto Ursino
 * @since 07/01/2020
 */
public class InstructionsActivity extends AppCompatActivity implements PasswordDialogListener {

    PasswordManager passwordManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        passwordManager = new PasswordManager();

        Button setPassButton = findViewById(R.id.set_pass_button);
        setPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPassDialog();
            }
        });
    }

    /**
     * 1) Saves the password through {@link PasswordManager}
     * 2) Starts the {@link MainActivity}
     * 3) Closes the {@link InstructionsActivity}
     *
     * @param password that will be stored
     * @param context  of the application
     * @author Alberto Ursino
     */
    @Override
    public void onPasswordSet(String password, Context context) {
        passwordManager.setPassword(context, password);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        //This activity will no longer be necessary
        this.finish();
    }

    /**
     * If the user doesn't set any password, a toast is displayed telling him that the app needs it
     *
     * @param context of the application
     */
    @Override
    public void onPasswordNotSet(Context context) {
        Toast.makeText(context, getString(R.string.toast_password_must_be_set), Toast.LENGTH_LONG).show();
    }

    //Useless in this activity
    @Override
    public void onPasswordChanged(String password, Context context) {
    }

    //Useless in this activity
    @Override
    public void onPasswordNotChanged(Context context) {
    }

    /**
     * Creates the dialog used to insert a non empty password or exit/abort
     *
     * @author Alberto Ursino
     */
    void openPassDialog() throws IllegalCommandException {
        PasswordDialog passwordDialog;
        passwordDialog = new PasswordDialog(SET_PASS_COMMAND, getApplicationContext());
        passwordDialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }
}
