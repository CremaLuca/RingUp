package com.gruppo4.ringUp;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.listeners.SMSSentListener;
import com.gruppo4.permissions.PermissionsHandler;
import com.gruppo4.ringUp.structure.AppManager;
import com.gruppo4.ringUp.structure.NotificationHandler;
import com.gruppo4.ringUp.structure.PasswordManager;
import com.gruppo4.ringUp.structure.ReceivedMessageListener;
import com.gruppo4.ringUp.structure.RingCommand;
import com.gruppo4.ringUp.structure.RingCommandHandler;
import com.gruppo4.ringUp.structure.RingtoneHandler;
import com.gruppo4.ringUp.structure.dialog.CommandDialog;
import com.gruppo4.ringUp.structure.dialog.PasswordDialog;
import com.gruppo4.ringUp.structure.dialog.PasswordDialogListener;
import com.gruppo4.ringUp.structure.exceptions.IllegalCommandException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;

/**
 * @author Gruppo4
 */
public class MainActivity extends AppCompatActivity implements PasswordDialogListener {

    public static final String APP_NAME = "RingUp";

    private Button ringButton;
    private TextView adviceTextView;
    private static final String IDENTIFIER = RingCommandHandler.SIGNATURE;
    private static final int WAIT_TIME_RING_BTN_ENABLED = 10 * 1000;
    private static int timerValue = WAIT_TIME_RING_BTN_ENABLED;
    private static String adviceText = "Wait " + timerValue + " seconds for a new ring";
    static final int CHANGE_PASS_COMMAND = 0;
    static final int SET_PASS_COMMAND = 1;
    static final String DIALOG_TAG = "Device Password";

    private PasswordManager passwordManager;
    public static final String CHANNEL_NAME = "TestChannelName";
    public static final String CHANNEL_ID = "123";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Stop Ringtone Notification";
    private static final int COUNTDOWN_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        passwordManager = new PasswordManager(context);

        Intent preActIntent;
        //If the permissions are not given, the permissionsActivity is opened
        if (!PermissionsHandler.checkPermissions(context, PermissionsActivity.permissions)) {
            preActIntent = new Intent(context, PermissionsActivity.class);
            startActivity(preActIntent);
            this.finish();
            //If the permissions are all granted then checks if a password is stored in memory: if NOT then open the instructionsActivity
        } else if (!passwordManager.isPassSaved()) {
            preActIntent = new Intent(context, InstructionsActivity.class);
            startActivity(preActIntent);
            this.finish();
        }

        //Setting up the action bar
        Toolbar toolbar = findViewById(R.id.actionBar);
        toolbar.setTitle(APP_NAME);
        toolbar.setTitleTextColor(getColor(R.color.black));
        setSupportActionBar(toolbar);

        createNotificationChannel();

        //Only if the activity is started by a service
        startFromService();

        //Setting up the custom listener in order to receive messages
        SMSManager.getInstance().setReceivedListener(ReceivedMessageListener.class, context);
        adviceTextView = findViewById(R.id.advice_text_view);
    }

    /**
     * Callback for ring button
     *
     * @param view
     */
    public void onRingButtonClick(View view) {
        CommandDialog dialog = new CommandDialog(getApplicationContext(), findViewById(R.id.main_activity_layout));
        dialog.show(getSupportFragmentManager(), "CommandDialog");
    }

    //**************************************SEND_COMMAND**************************************

    /**
     * Method used to send the ring command through the {@link AppManager#sendCommand(Context, RingCommand, SMSSentListener)} method
     *
     * @author Alberto Ursino
     * @author Luca Crema
     */
    /**public void sendRingCommand() {

        if (password.isEmpty() && phoneNumber.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_pass_phone_number_absent), Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_phone_number_absent), Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_password_absent), Toast.LENGTH_SHORT).show();
        } else {
            try {
                //Creation of the ring command
                final RingCommand ringCommand = new RingCommand(new SMSPeer(phoneNumber), IDENTIFIER + password);

                AppManager.getInstance().sendCommand(getApplicationContext(), ringCommand, (SMSMessage message, SMSMessage.SentState sentState) -> {
                    Snackbar.make(findViewById(R.id.main_activity_layout),getString(R.string.toast_message_sent_listener) + " " + phoneNumber, Snackbar.LENGTH_LONG).show();
                });
                ringButton.setEnabled(false);
                adviceTextView.setText(adviceText);

                //Button disabling
                new CountDownTimer(WAIT_TIME_RING_BTN_ENABLED, COUNTDOWN_INTERVAL) {

                    public void onTick(long millisUntilFinished) {
                        timerValue = (int) millisUntilFinished;
                        adviceTextView.setText("Wait " + timerValue / COUNTDOWN_INTERVAL + " seconds for send a new find request");
                    }

                    public void onFinish() {
                        if (!ringButton.isEnabled())
                            ringButton.setEnabled(true);
                        adviceTextView.setText("");
                        timerValue = WAIT_TIME_RING_BTN_ENABLED;
                    }
                }.start();

            } catch (InvalidTelephoneNumberException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_invalid_phone_number), Toast.LENGTH_SHORT).show();
            }


     }
     }*/

    //**************************************MENU**************************************

    /**
     * Method used to show up the {@link menu/app_menu.xml}
     *
     * @author Alberto Ursino
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Called when the user selects an item from the {@link menu/app_menu.xml}
     *
     * @author Alberto Ursino
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_password_menu_item:
                openDialog(CHANGE_PASS_COMMAND);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //**************************************PASSWORD_DIALOG**************************************

    /**
     * Creates the dialog used to insert a non empty password or exit/abort
     *
     * @param command Specified type of dialog that should be opened, represented by an int value
     * @throws IllegalCommandException usually thrown when the dialog command passed is not valid
     * @author Alberto Ursino
     */
    void openDialog(int command) throws IllegalCommandException {
        PasswordDialog passwordDialog;
        if (command == CHANGE_PASS_COMMAND) {
            passwordDialog = new PasswordDialog(CHANGE_PASS_COMMAND, getApplicationContext());
            passwordDialog.show(getSupportFragmentManager(), DIALOG_TAG);
        } else {
            throw new IllegalCommandException();
        }
    }

    //Useless in this activity
    @Override
    public void onPasswordSet(String password, Context context) {
    }

    //Useless in this activity
    @Override
    public void onPasswordNotSet(Context context) {
    }

    @Override
    public void onPasswordChanged(String password, Context context) {
        Toast.makeText(context, getString(R.string.toast_password_changed), Toast.LENGTH_SHORT).show();
        passwordManager.setPassword(password);
    }

    @Override
    public void onPasswordNotChanged(Context context) {
        Toast.makeText(context, getString(R.string.toast_password_not_changed), Toast.LENGTH_LONG).show();
    }

    //**************************************NOTIFICATION**************************************

    /**
     * Creates the NotificationChannel, but only on API 26+ because
     * the NotificationChannel class is new and not in the support library
     * <p>
     * Register the channel with the system; you can't change the importance
     * or other notification behaviors after this
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //IMPORTANCE_HIGH makes pop-up the notification
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            } else {
                Log.d("MainActivity", "getSystemService(NotificationManager.class), in createNotificationChannel method, returns a null object");
            }
        }
    }

    /**
     * Updates intent obtained from a service's call
     *
     * @param intent to handle
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
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case NotificationHandler.ALERT_ACTION: {
                    createStopRingDialog();
                    Log.d("MainActivity", "Creating StopRingDialog...");
                    break;
                }
                default:
                    break;
            }
        } else {
            Log.d("MainActivity", "getIntent, in startFromService method, returns a null intent");
        }
    }

    /**
     * Creates and shows AlertDialog with one option:
     * [stop] --> stop the ringtone and cancel the notification
     */
    private void createStopRingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getString(R.string.text_stop_ring_dialog));
        builder.setCancelable(true);
        Log.d("MainActivity", "StopRingDialog created");

        builder.setPositiveButton(
                getString(R.string.text_notification_button), (dialogInterface, i) -> {
                    RingtoneHandler.getInstance().stopRingtone(AppManager.defaultRing);
                    Log.d("MainActivity", "Stopping ringtone");
                    //cancel the right notification by id
                    int id = getIntent().getIntExtra(NotificationHandler.NOTIFICATION_ID, -1);
                    NotificationManagerCompat.from(getApplicationContext()).cancel(id);
                    NotificationHandler.notificationFlag = false;
                    Log.d("MainActivity", "Notification " + id + " cancelled");
                    dialogInterface.dismiss();
                }
        );

        AlertDialog alert = builder.create();
        alert.show();
        Log.d("MainActivity", "Showing StopRingDialog...");
    }

}


