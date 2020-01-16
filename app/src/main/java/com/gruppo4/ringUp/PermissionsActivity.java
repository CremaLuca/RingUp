package com.gruppo4.ringUp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gruppo4.permissions.PermissionsHandler;
import com.gruppo4.ringUp.structure.PasswordManager;

import static com.gruppo4.ringUp.MainActivity.BAR_TITLE;
import static com.gruppo4.ringUp.MainActivity.appName;

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

        //Setting up the default ringUp action bar
        Toolbar toolbar = findViewById(R.id.permissionsToolbar);
        toolbar.setTitle(BAR_TITLE);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        TextView SMSPermissionsTextView = findViewById(R.id.sms_permissions_text_view);
        TextView ContactsPermissionsTextView = findViewById(R.id.contacts_permissions_text_view);
        //Setting up the permissions text
        String smsPermissionsText = getString(R.string.permissions_text_SMS);
        String contactsPermissionsText = getString(R.string.permissions_text_CONTACTS);
        SpannableString ssSMS = new SpannableString(smsPermissionsText);
        SpannableString ssContacts = new SpannableString(contactsPermissionsText);
        ssSMS.setSpan(new ForegroundColorSpan(getColor(R.color.ring_up_color)), smsPermissionsText.indexOf(appName), smsPermissionsText.indexOf(appName) + appName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssContacts.setSpan(new ForegroundColorSpan(getColor(R.color.ring_up_color)), contactsPermissionsText.indexOf(appName), contactsPermissionsText.indexOf(appName) + appName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SMSPermissionsTextView.setText(ssSMS);
        ContactsPermissionsTextView.setText(ssContacts);

        Button permissionsButton = findViewById(R.id.request_permissions_button);
        permissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionsHandler.requestPermissions(activity, getApplicationContext(), permissions);
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
        if (!(new PermissionsHandler().checkAllPermissions(getApplicationContext(), permissions)))

            Toast.makeText(getApplicationContext(), getString(R.string.toast_app_needs_permissions), Toast.LENGTH_SHORT).show();
        else {
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