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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eis.smslibrary.SMSHandler;
import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.RingApplication.structure.AppManager;
import com.gruppo4.RingApplication.structure.Interfaces.PermissionInterface;
import com.gruppo4.RingApplication.structure.PasswordManager;
import com.gruppo4.RingApplication.structure.ReceivedMessageListener;
import com.gruppo4.RingApplication.structure.RingCommand;
import com.gruppo4.RingApplication.structure.RingCommandHandler;
import com.gruppo4.RingApplication.structure.RingtoneHandler;
import com.gruppo4.RingApplication.structure.dialog.PasswordDialog;
import com.gruppo4.RingApplication.structure.dialog.PasswordDialogListener;
import com.gruppo4.RingApplication.structure.exceptions.IllegalCommandException;
import com.gruppo_4.preferences.PreferencesManager;

/**
 * @author Gruppo4
 */
public class MainActivity extends AppCompatActivity implements PasswordDialogListener, PermissionInterface {

    private static final int PERMISSION_CODE = 0;
    static final int CHANGE_PASS_COMMAND = 0;
    private static final int SET_PASS_COMMAND = 1;
    private static final String SPLIT_CHARACTER = RingCommandHandler.SPLIT_CHARACTER;
    private static final int WAIT_TIME_PERMISSION = 2000;
    private static final int WAIT_TIME_RINGTONE = 30 * 1000; //30 seconds by default
    private Ringtone ringtone;
    private EditText phoneNumberField;
    private EditText passwordField;
    private Button ringButton;
    private static PasswordManager passwordManager = null;
    public static final String SETTINGS_NAME = "Settings";
    public final static String TIMEOUT_TIME_PREFERENCES_KEY = "Timer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.actionBar));

        Context context = getApplicationContext();

        SMSHandler smsHandler = SMSHandler.getInstance();
        RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();

        passwordManager = new PasswordManager(context);

        setupTimerValue();

        ringtone = ringtoneHandler.getDefaultRingtone(getApplicationContext());
        phoneNumberField = findViewById(R.id.phone_number_field);
        passwordField = findViewById(R.id.password_field);
        ringButton = findViewById(R.id.ring_button);

        /**
         * Two cases can occur:
         * 1st) The user open the app for the 1st time -> set a not empty password -> grant permissions.
         * 2nd) The user open the app for the 1st time -> set a not empty password -> DON'T grant permissions -> Re-enter the application -> Has the possibility to grant permits again ↺
         * Both satisfied by the following if:
         */
        //If there's a password stored and the receive permissions are granted -> setup the SMSHandler
        if (passwordManager.isPassSaved() && (context.checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED))
            smsHandler.setup(context);

        //Password stored: if NOT -> open the dialog, if YES -> check permissions
        if (!passwordManager.isPassSaved()) {
            openDialog();
        } else {
            checkPermission();
        }

        smsHandler.setReceivedListener(new ReceivedMessageListener(context));

        ringButton.setOnClickListener(v -> sendRingCommand());

    }

    /**
     * Creates the dialog used to insert a non empty password or exit/abort
     *
     * @throws IllegalCommandException
     */
    void openDialog() throws IllegalCommandException {
        if (PasswordDialog.isCommandSetPass(SET_PASS_COMMAND)) {
            PasswordDialog passwordDialog = new PasswordDialog(SET_PASS_COMMAND);
            passwordDialog.show(getSupportFragmentManager(), "Device Password");
        } else {
            throw new IllegalCommandException();
        }
    }

    /**
     * Method used to stop the ringtone when the user presses on the button
     *
     * @param view of the application
     */
    public void stopRingtone(View view) {
        RingtoneHandler.getInstance().stopRingtone(ringtone);
    }

    /**
     * Method used to send the ring command when the user presses on the "RING" button
     */
    public void sendRingCommand() {
        if (passwordField.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(), "Insert a password", Toast.LENGTH_SHORT).show();
        else {
            final RingCommand ringCommand = new RingCommand(new SMSPeer(phoneNumberField.getText().toString()), createPassword(passwordField.getText().toString()));
            try {
                AppManager.getInstance().sendCommand(getApplicationContext(), ringCommand, (message, sentState) ->
                        Toast.makeText(MainActivity.this, String.format("Command sent to %s", ringCommand.getPeer()), Toast.LENGTH_SHORT).show());
            } catch (InvalidTelephoneNumberException e) {
                Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
            } catch (InvalidSMSMessageException e) {
                //This should never happen, the message is a prefixed code, user has nothing to do with it
            }
        }
    }

    /**
     * @param password given by the user
     * @return the passwords with a special character at the beginning
     */
    private String createPassword(String password) {
        return SPLIT_CHARACTER + password;
    }

    /**
     * Opens a new activity with the application settings
     */
    public void openSettingsActivity() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Controls if a timer value is present on memory, if not we need a default value -> 30 seconds
     */
    private void setupTimerValue() {
        if (PreferencesManager.getInt(getApplicationContext(), TIMEOUT_TIME_PREFERENCES_KEY) == (PreferencesManager.DEFAULT_INTEGER_RETURN)) {
            PreferencesManager.setInt(getApplicationContext(), TIMEOUT_TIME_PREFERENCES_KEY, WAIT_TIME_RINGTONE);
        }
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
            openSettingsActivity();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            SMSHandler.getInstance().setup(getApplicationContext());
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onPasswordSet(String password, Context context) {
        passwordManager.setPassword(password);
        //Ask for permission after a short period of time
        waitForPermissions(WAIT_TIME_PERMISSION);
    }

    @Override
    public void checkPermission() {
        if (!(getApplicationContext().checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED))
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSION_CODE);
    }

    @Override
    public void waitForPermissions(int time) {
        Handler handler = new Handler();
        handler.postDelayed(() -> checkPermission(), time);
    }
}