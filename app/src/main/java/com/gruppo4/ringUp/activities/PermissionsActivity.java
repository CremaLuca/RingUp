package com.gruppo4.ringUp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.ringUp.R;
import com.gruppo4.ringUp.permissions.PermissionsHandler;
import com.gruppo4.ringUp.structure.PasswordManager;

/**
 * The following activity aims to inform the user about why the application needs certain permissions.
 * Necessary permissions for this application (RingUp) are:
 * - SEND_SMS and RECEIVE_SMS;
 * - READ_CONTACTS.
 * The {@link PermissionsActivity} (PA) is directly linked to the {@link InstructionsActivity} (IA) and the {@link MainActivity} (MA).
 * The {@link #changeActivity()} method, depending on the situation, takes the user to one or the other activity once he has given the necessary permissions.
 * E.g: After the user has given the necessary permissions in the PA, IF HE HAS ALREADY SET A PASSWORD, it means that he has already passed through the IA, therefore, he will be directed directly to the MA.
 * ON THE CONTRARY, he will have to go through the IA to set the password for the first time and then continue to the MA.
 *
 * @author Alberto Ursino
 * @version 2.0
 * @since 07/01/2020
 */
public class PermissionsActivity extends AppCompatActivity {

    static final String[] permissions = new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_CONTACTS};
    private String[] requiredPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        Button permissionsButton = findViewById(R.id.request_permissions_button);
        permissionsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @see PermissionsHandler in order to know how the {@link PermissionsHandler#getDeniedPermissions(Context, String[])} method works.
             */
            @Override
            public void onClick(View v) {
                requiredPermissions = PermissionsHandler.getDeniedPermissions(getApplicationContext(), permissions);
                //Maybe all the permissions are already granted
                if (requiredPermissions.length != 0)
                    requestPermissions(requiredPermissions, PermissionsHandler.REQUEST_CODE);
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_permissions_already_granted), Toast.LENGTH_SHORT).show();
                    changeActivity();
                }
            }
        });

    }

    /**
     * Android method used as a callback for the permissions request.
     * If the user has not given all the permissions he can't continue to the next activity.
     * In this case the dialog will close showing an adequate toast.
     * On the contrary, the user will be directed to the new activity according to the situation.
     *
     * @param requestCode  The code has to be equal to {@link PermissionsHandler#REQUEST_CODE}
     * @param permissions  Permissions in the {@link #requiredPermissions} string array, requested with the {@link Activity#requestPermissions(String[], int)} method
     * @param grantResults Result of the callback:
     *                     0 -> Permissions GRANTED;
     *                     -1 -> Permissions NOT GRANTED
     * @author Implemented by Alberto Ursino
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean check = true;
        if (requestCode == PermissionsHandler.REQUEST_CODE) {
            for (int singleResult : grantResults) {
                if (singleResult == -1) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_app_needs_permissions), Toast.LENGTH_SHORT).show();
                    check = false;
                    break;
                }
            }
            if (check)
                changeActivity();
        }
    }

    /**
     * Method called when the user has given all necessary permissions.
     * If the user has already set a password then the user will be directed directly to the {@link MainActivity}, on the contrary he will go to the {@link InstructionsActivity}.
     *
     * @author Alberto Ursino
     */
    private void changeActivity() {
        Intent nextActivity;
        if (PasswordManager.isPassSaved(getApplicationContext())) {
            //Start the MainActivity
            nextActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(nextActivity);
            //This activity will no longer be necessary
            this.finish();
        } else {
            //Start the InstructionsActivity
            nextActivity = new Intent(getApplicationContext(), InstructionsActivity.class);
            startActivity(nextActivity);
            //This activity will no longer be necessary
            this.finish();
        }
    }

}