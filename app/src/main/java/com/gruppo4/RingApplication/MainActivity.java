package com.gruppo4.RingApplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gruppo4.RingApplication.ringCommands.*;
import com.gruppo4.RingApplication.ringCommands.dialog.*;
import com.gruppo4.RingApplication.ringCommands.exceptions.*;
import com.gruppo4.sms.dataLink.*;
import com.gruppo4.sms.dataLink.exceptions.*;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

/**
 * @author Alberto Ursino, Alessandra Tonin
 * <p>
 * Code reviewed by Bortoletti and Barca
 * <p>
 * Usefull help: https://www.youtube.com/watch?v=j-3L3CgYXkU
 */
public class MainActivity extends AppCompatActivity implements PasswordDialogListener {

    private static final int PERMISSION_CODE = 0;
    private static final int APPLICATION_CODE = 1;
    static final int CHANGE_PASS_COMMAND = 0;
    private static final int SET_PASS_COMMAND = 1;
    private static final String SPLIT_CHARACTER = RingCommandHandler.SPLIT_CHARACTER;
    private static final int WAIT_TIME = 2000;
    private Ringtone RINGTONE;
    private EditText PHONE_NUMBER;
    private EditText SEND_PASSWORD;
    private static PasswordManager passwordManager = null;
    private static final String SETTINGS_NAME = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.action_bar));

        final Context context = getApplicationContext();
        SMSHandler smsHandler = SMSHandler.getInstance(context);
        passwordManager = new PasswordManager(context);

        RINGTONE = RingtoneHandler.getDefaultRingtone(getApplicationContext());
        PHONE_NUMBER = findViewById(R.id.telephoneNumber);
        SEND_PASSWORD = findViewById(R.id.password);

        /**
         * Two cases can occur:
         * 1st) The user open the app for the 1st time -> set a not empty password -> grant permissions.
         * 2nd) The user open the app for the 1st time -> set a not empty password -> DON'T grant permissions -> Re-enter the application -> Has the possibility to grant permits again ↺
         * Both satisfied by the following if:
         */

        //If there's a password stored and the permissions are granted -> setup the SMSHandler
        if (passwordManager.isPassSaved() && SMSHandler.checkReceivePermission(context))
            smsHandler.setup(APPLICATION_CODE);

        //Password stored: if NOT -> open the dialog, if YES -> check permissions
        if (!passwordManager.isPassSaved()) {
            openDialog(SET_PASS_COMMAND);
        } else {
            checkPermission(context);
        }

        smsHandler.setReceivedMessageListener(ReceivedMessageListener.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().toString().equals(SETTINGS_NAME))
            openSettings();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            SMSHandler.getInstance(getApplicationContext()).setup(APPLICATION_CODE);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void applyText(String password, Context context) {
        passwordManager.setPassword(password);
        Toast.makeText(getApplicationContext(), "Password saved", Toast.LENGTH_SHORT).show();
        waitForPermissions(WAIT_TIME);
    }

    /**
     * @param password given by the user
     * @return the passwords with a special character at the beginning
     */
    private String createPassword(String password) {
        return SPLIT_CHARACTER + password;
    }

    /**
     * Creates the dialog used to insert a non empty password or exit/abort
     *
     * @param command to open the right dialog
     * @throws IllegalCommandException
     */
    void openDialog(int command) throws IllegalCommandException {
        if (PasswordDialog.isCommandSetPass(command)) {
            PasswordDialog passwordDialog = new PasswordDialog(SET_PASS_COMMAND);
            passwordDialog.show(getSupportFragmentManager(), "Device Password");
        } else if (PasswordDialog.isCommandChangePass(CHANGE_PASS_COMMAND)) {
            PasswordDialog passwordDialog = new PasswordDialog(CHANGE_PASS_COMMAND);
            passwordDialog.show(getSupportFragmentManager(), "Change Password");
        } else {
            throw new IllegalCommandException();
        }
    }

    /**
     * Simple method used to check permissions
     *
     * @param context of the application
     */
    void checkPermission(Context context) {
        if (!SMSHandler.checkReceivePermission(context))
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSION_CODE);
    }

    /**
     * @param time to wait before checking permits
     */
    void waitForPermissions(int time) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermission(getApplicationContext());
            }
        }, time);
    }

    /**
     * Method used to stop the ringtone when the user presses on the button
     *
     * @param view of the application
     */
    public void stopRingtone(View view) {
        RingtoneHandler.stopRingtone(RINGTONE);
    }

    /**
     * Method used to send the ring command when the user presses on the "RING" button
     *
     * @param view of the application
     * @throws InvalidTelephoneNumberException
     * @throws InvalidSMSMessageException
     */
    public void sendRingCommand(View view) throws InvalidTelephoneNumberException, InvalidSMSMessageException {
        final RingCommand ringCommand = new RingCommand(new SMSPeer(PHONE_NUMBER.getText().toString()), createPassword(SEND_PASSWORD.getText().toString()));
        AppManager.sendCommand(getApplicationContext(), ringCommand, new SMSSentListener() {
            @Override
            public void onSMSSent(SMSMessage message, SMSMessage.SentState sentState) {
                Toast.makeText(MainActivity.this, String.format("Command sent to %s", ringCommand.getPeer()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Opens a new activity with the application settings
     */
    public void openSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }
}
