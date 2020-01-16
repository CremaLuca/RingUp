package com.gruppo4.ringUp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.permissions.PermissionsHandler;
import com.gruppo4.ringUp.structure.PasswordManager;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

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

        PermissionsHandler permissionsHandler = new PermissionsHandler();
        Activity activity = this;

        TextView permissionsTextView = findViewById(R.id.permissions_text_view);
        permissionsTextView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        Button permissionsButton = findViewById(R.id.request_permissions_button);
        permissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionsHandler.requestPermissions(activity, permissionsHandler.getDeniedPermissions(getApplicationContext(), permissions));
            }
        });

    }

    /**
     * Callback for the permissions request.
     * If the user has not granted all the permissions he cannot continue and a toast will be displayed telling that the app needs the permissions.
     * Otherwise checks if a password is saved in the memory:
     * If YES, the {@link MainActivity} is opened by closing the {@link PermissionsActivity};
     * If NOT, the {@link InstructionsActivity} is opened by closing the {@link PermissionsActivity}.
     *
     * @author Alberto Ursino
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean check = true;

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == -1) {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_app_needs_permissions), Toast.LENGTH_SHORT).show();
                check = false;
                break;
            }
        }

        if (check) {
            Intent nextActivity;
            if (new PasswordManager(getApplicationContext()).isPassSaved()) {
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
}