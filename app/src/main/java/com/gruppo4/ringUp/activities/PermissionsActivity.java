package com.gruppo4.ringUp.activities;

import android.Manifest;
import android.app.Activity;
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
 * Class used to inform the user of which permissions the app needed and requires them
 *
 * @author Francesco Bau'
 * @author Alberto Ursino
 * @since 07/01/2020
 */
public class PermissionsActivity extends AppCompatActivity {

    /**
     * Necessary permissions: SEND_SMS, RECEIVE_SMS and READ_CONTACTS
     */
    static final String[] permissions = new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        Button permissionsButton = findViewById(R.id.request_permissions_button);
        permissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] requiredPermissions = PermissionsHandler.getDeniedPermissions(getApplicationContext(), permissions);
                //Maybe all the permissions are already granted
                if (requiredPermissions.length != 0)
                    requestPermissions(PermissionsHandler.getDeniedPermissions(getApplicationContext(), permissions), PermissionsHandler.REQUEST_CODE);
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_permissions_already_granted), Toast.LENGTH_SHORT).show();
                    changeActivity();
                }
            }
        });

    }

    /**
     * Callback for the permissions request.
     * If the user has not granted all the permissions he cannot continue and a toast will be displayed telling that the app needs the permissions, otherwise calls {@link #changeActivity()}.
     * If the user has selected the option "Don't show it again" must go to the app settings and give them.
     *
     * @param requestCode  has to be equals to {@link PermissionsHandler#REQUEST_CODE}
     * @param permissions  requested by the {@link Activity#requestPermissions(String[], int)}
     * @param grantResults Result of this callback: 0 -> Permissions GRANTED; -1 -> Permissions NOT GRANTED
     * @author Alberto Ursino
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean check = true;

        if (requestCode == PermissionsHandler.REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
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
     * Starts the next activity, it can be the {@link InstructionsActivity} or the {@link MainActivity} if a password is already set.
     * This activity will then be closed.
     *
     * @author Alberto Ursino
     */
    public void changeActivity() {
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