package com.gruppo4.RingApplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

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
    private EditText phoneNumberField;
    private EditText passwordField;
    private Button ringButton;
    private PasswordManager passwordManager;
    private static final int SET_PASS_COMMAND = 1;
    private static final String IDENTIFIER = RingCommandHandler.SPLIT_CHARACTER;
    private static final int WAIT_TIME_RINGTONE = 30 * 1000; //30 seconds by default
    private static final int WAIT_TIME_PERMISSION = 1500;
    public static final String SETTINGS_NAME = "Settings";
    public final static String TIMEOUT_TIME_PREFERENCES_KEY = "Timer";
    public static final String CHANNEL_NAME = "TestChannelName";
    public static final String CHANNEL_ID = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the action bar
        setSupportActionBar(findViewById(R.id.actionBar));
        createNotificationChannel();

        //Checking the if permissions are granted
        requestPermissions();

        //Setting up the timer
        setupTimerValue();

        //Only if the activity is started by a service
        startFromService();

        passwordManager = new PasswordManager(getApplicationContext());
        phoneNumberField = findViewById(R.id.phone_number_field);
        passwordField = findViewById(R.id.password_field);
        ringButton = findViewById(R.id.ring_button);

        //A dialog will be opened if password is not stored
        if (!passwordManager.isPassSaved())
            openDialog(SET_PASS_COMMAND);

        //Setting up the listener in order to receive messages
        SMSManager.getInstance().setReceivedListener(ReceivedMessageListener.class, getApplicationContext());

        ringButton.setOnClickListener(v -> sendRingCommand());
    }

    /**
     * Creates the dialog used to insert a non empty password or exit/abort
     *
     * @param command Specified type of dialog that should be opened, represented by an int value
     * @throws IllegalCommandException usually thrown when the dialog command passed is not valid
     * @author Alberto Ursino
     */
    void openDialog(int command) throws IllegalCommandException {
        switch (command) {
            case SET_PASS_COMMAND:
                PasswordDialog passwordDialog = new PasswordDialog(SET_PASS_COMMAND);
                passwordDialog.show(getSupportFragmentManager(), "Device Password");
                break;
            default:
                throw new IllegalCommandException();
        }
    }

    /**
     * Creates the NotificationChannel, but only on API 26+ because
     * the NotificationChannel class is new and not in the support library
     * <p>
     * Register the channel with the system; you can't change the importance
     * or other notification behaviors after this
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "TestChannelDescription";
            //IMPORTANCE_HIGH makes pop-up the notification
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Updates intent obtained from a service's call
     *
     * @param intent to updates
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        startFromService();
    }

    /**
     * Manages action from intent
     */
    private void startFromService() {
        Log.d("MainActivity", "startFromService called");
        Intent intent = getIntent();
        if (intent != null) {
            switch (intent.getAction()) {
                case AppManager.ALERT_ACTION: {
                    createStopRingDialog();
                    Log.d("MainActivity", "Creating StopRingDialog...");
                    break;
                }

                default:
                    break;
            }
        }
    }

    /**
     * Creates and shows AlertDialog with one option:
     * [stop] --> stop the ringtone and cancel the notification
     */
    private void createStopRingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Your phone is ringing, stop it from here if you want");
        builder.setCancelable(true);
        Log.d("MainActivity", "StopRingDialog created");

        builder.setPositiveButton(
                "Stop", (dialogInterface, i) -> {
                    AppManager.getInstance().stopRingtone();
                    Log.d("MainActivity", "Stopping ringtone");
                    //cancel the right notification by id
                    int id = getIntent().getIntExtra(AppManager.NOTIFICATION_ID, -1);
                    NotificationManagerCompat.from(getApplicationContext()).cancel(id);
                    Log.d("MainActivity", "Notification " + id + " cancelled");
                    dialogInterface.dismiss();
                }
        );

        AlertDialog alert = builder.create();
        alert.show();
        Log.d("MainActivity", "Showing StopRingDialog...");
    }

    /**
     * Method used to send the ring command through the {@link AppManager#sendCommand(Context, RingCommand, SMSSentListener)} method
     *
     * @author Alberto Ursino
     * @author Luca Crema
     */
    public void sendRingCommand() {
        String phoneNumber = phoneNumberField.getText().toString();
        String password = passwordField.getText().toString();

        if (password.isEmpty())
            Toast.makeText(getApplicationContext(), "Insert a password", Toast.LENGTH_SHORT).show();
        else {
            final RingCommand ringCommand = new RingCommand(new SMSPeer(phoneNumber), IDENTIFIER + password);
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
     * Opens a new activity with the application settings
     *
     * @author Alberto Ursino
     */
    public void openSettingsActivity() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Controls if a timer value is present on memory, if not we need a default value -> 30 seconds
     *
     * @author Luca Crema
     */
    private void setupTimerValue() {
        if (PreferencesManager.getInt(getApplicationContext(), TIMEOUT_TIME_PREFERENCES_KEY) == (PreferencesManager.DEFAULT_INTEGER_RETURN)) {
            PreferencesManager.setInt(getApplicationContext(), TIMEOUT_TIME_PREFERENCES_KEY, WAIT_TIME_RINGTONE);
        }
    }

    /**
     * @return true if the app has both RECEIVE_SMS and SEND_SMS permissions, false otherwise
     * @author Alberto Ursino
     */
    public boolean checkPermissions() {
        Context context = getApplicationContext();
        return (context.checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) &&
                (context.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Checks if permissions are granted, if not then requests them to the user
     *
     * @author Alberto Ursino
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
