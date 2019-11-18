package com.gruppo4.RingApplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.gruppo4.RingApplication.ringCommands.AppManager;
import com.gruppo4.RingApplication.ringCommands.PasswordManager;
import com.gruppo4.RingApplication.ringCommands.ReceivedMessageListener;
import com.gruppo4.RingApplication.ringCommands.RingCommand;
import com.gruppo4.RingApplication.ringCommands.RingCommandHandler;
import com.gruppo4.RingApplication.ringCommands.RingtoneHandler;
import com.gruppo4.RingApplication.ringCommands.dialog.PasswordDialog;
import com.gruppo4.RingApplication.ringCommands.dialog.PasswordDialogListener;
import com.gruppo4.RingApplication.ringCommands.exceptions.IllegalCommandException;
import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

/**
 * @author Alberto Ursino, Alessandra Tonin
 *
 * Code review for Bortoletti and Barca
 */
public class MainActivity extends AppCompatActivity implements PasswordDialogListener {

    private static final int PERMISSION_CODE = 0;
    private static final int APPLICATION_CODE = 1;
    static final int CHANGE_PASS_COMMAND = 0;
    private static final int SET_PASS_COMMAND = 1;
    private static final String SPLIT_CHARACTER = RingCommandHandler.SPLIT_CHARACTER;
    private static final int WAIT_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Ring Application");

        final Context context = getApplicationContext();
        SMSHandler smsHandler = SMSHandler.getInstance(context);

        /**
         * Two cases can occur:
         * 1st) The user open the app for the 1st time -> set a valid password -> grant permissions.
         * 2nd) The user open the app for the 1st time -> set a valid password -> DON'T grant permissions -> Re-enter the application -> Has the possibility to grant permits again ↺
         * Both satisfied by the following if:
         */

        //If there's a password stored and the permissions are granted -> setup the SMSHandler
        if (PasswordManager.isPassSaved(context) && SMSHandler.checkReceivePermission(context))
            smsHandler.setup(APPLICATION_CODE);

        //Password stored: if NOT -> open the dialog, if YES -> check permissions
        if (!PasswordManager.isPassSaved(context)) {
            openDialog(SET_PASS_COMMAND);
        } else {
            checkPermission(context);
        }

        final Ringtone RINGTONE = RingtoneHandler.getDefaultRingtone(context);
        final EditText PHONE_NUMBER = findViewById(R.id.telephoneNumber);
        final EditText SEND_PASSWORD = findViewById(R.id.password);
        final Button RING_BUTTON = findViewById(R.id.button);
        final Button STOP_BUTTON = findViewById(R.id.stop);
        final ImageButton SETTINGS_BUTTON = findViewById(R.id.settingsButton);

        smsHandler.setReceivedMessageListener(ReceivedMessageListener.class);

        //Send the ring command
        RING_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendRingCommand(PHONE_NUMBER.getText().toString(), SEND_PASSWORD.getText().toString(), context);
                } catch (InvalidSMSMessageException e) {
                    e.printStackTrace();
                } catch (InvalidTelephoneNumberException e) {
                    e.printStackTrace();
                }
            }
        });

        //Button used to stop the ringtone
        STOP_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RingtoneHandler.stopRingtone(RINGTONE);
            }
        });

        SETTINGS_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

    }

    /**
     * Opens a new activity with the application settings
     */
    private void openSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
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
        PasswordManager.setPassword(context, password);
        Toast.makeText(getApplicationContext(), "Password saved", Toast.LENGTH_SHORT).show();
        waitForPermissions(WAIT_TIME);
    }

    /**
     * @param destination the telephone number of the person you want to send the ring command
     * @param password    to send
     * @param context     of the application
     * @throws InvalidSMSMessageException
     * @throws InvalidTelephoneNumberException
     */
    private void sendRingCommand(String destination, String password, Context context) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        final RingCommand ringCommand = new RingCommand(new SMSPeer(destination), createPassword(password));
        AppManager.sendCommand(context, ringCommand, new SMSSentListener() {
            @Override
            public void onSMSSent(SMSMessage message, SMSMessage.SentState sentState) {
                Toast.makeText(MainActivity.this, String.format("Command sent to %s", ringCommand.getPeer()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param password given by the user
     * @return the passwords with a special character at the beginning
     */
    private String createPassword(String password) {
        return SPLIT_CHARACTER + password;
    }

    /**
     * Creates the dialog used to insert a valid password or exit/abort
     *
     * @param command to open the right dialog
     */
    void openDialog(int command) {
        if (PasswordDialog.isCommandSetPass(command)) {
            PasswordDialog passwordDialog = new PasswordDialog(SET_PASS_COMMAND);
            passwordDialog.show(getSupportFragmentManager(), "Device Password");
        } else if(PasswordDialog.isCommandChangePass(CHANGE_PASS_COMMAND)){
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

}
