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

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.eis.smslibrary.listeners.SMSSentListener;
import com.gruppo4.RingApplication.structure.*;
import com.gruppo4.RingApplication.structure.dialog.*;
import com.gruppo4.RingApplication.structure.exceptions.IllegalCommandException;

import it.lucacrema.preferences.PreferencesManager;

/**
 * @author Gruppo4
 */
public class MainActivity extends AppCompatActivity implements PasswordDialogListener {

    static final int CHANGE_PASS_COMMAND = 0;
    private static final int SET_PASS_COMMAND = 1;
    private static final String SPLIT_CHARACTER = RingCommandHandler.SPLIT_CHARACTER;
    private static final int WAIT_TIME_RINGTONE = 30 * 1000; //30 seconds by default
    private static final int WAIT_TIME_PERMISSION = 1500;
    private Ringtone ringtone;
    private EditText phoneNumberField;
    private EditText passwordField;
    private Button ringButton;
    private PasswordManager passwordManager;
    private RingtoneHandler ringtoneHandler;
    public static final String SETTINGS_NAME = "Settings";
    public final static String TIMEOUT_TIME_PREFERENCES_KEY = "Timer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the action bar
        setSupportActionBar(findViewById(R.id.actionBar));

        //Checking the if permissions are granted
        requestPermissions();

        //Setting up the timer
        setupTimerValue();

        ringtoneHandler = RingtoneHandler.getInstance();
        ringtone = ringtoneHandler.getDefaultRingtone(getApplicationContext());
        passwordManager = new PasswordManager(getApplicationContext());
        phoneNumberField = findViewById(R.id.phone_number_field);
        passwordField = findViewById(R.id.password_field);
        ringButton = findViewById(R.id.ring_button);

        //A dialog will be opened if password is not stored
        if (!passwordManager.isPassSaved())
            openDialog();

        SMSManager.getInstance().setReceivedListener(ReceivedMessageListener.class, getApplicationContext());

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
        String phoneNumber = phoneNumberField.getText().toString();
        String password = passwordField.getText().toString();

        if (password.isEmpty())
            Toast.makeText(getApplicationContext(), "Insert a password", Toast.LENGTH_SHORT).show();
        else {
            final RingCommand ringCommand = new RingCommand(new SMSPeer(phoneNumber), createPassword(password));
            try {
                SMSSentListener smsSentListener = (message, sentState) ->
                        Toast.makeText(getApplicationContext(), "Command sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
                AppManager.getInstance().sendCommand(getApplicationContext(), ringCommand, smsSentListener);
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

    /**
     * @return true if the app has both RECEIVE_SMS and SEND_SMS permissions, false otherwise
     */
    public boolean checkPermissions() {
        Context context = getApplicationContext();
        return (context.checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) &&
                (context.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Checks if permissions are granted, if not then requests them to the user
     */
    public void requestPermissions() {
        if (!checkPermissions())
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, 0);
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
        if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(getApplicationContext(), "The application needs these permissions", Toast.LENGTH_SHORT).show();
            //Let's wait the toast ends
            Handler handler = new Handler();
            handler.postDelayed(() -> requestPermissions(), WAIT_TIME_PERMISSION);
        }
    }

    @Override
    public void onPasswordSet(String password, Context context) {
        passwordManager.setPassword(password);
    }

}
