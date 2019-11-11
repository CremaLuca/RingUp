package com.gruppo4.RingApplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.RingApplication.ringCommands.AppManager;
import com.gruppo4.RingApplication.ringCommands.PasswordManager;
import com.gruppo4.RingApplication.ringCommands.ReceivedMessageListener;
import com.gruppo4.RingApplication.ringCommands.RingCommand;
import com.gruppo4.RingApplication.ringCommands.RingtoneHandler;
import com.gruppo4.RingApplication.ringCommands.dialog.PasswordDialog;
import com.gruppo4.RingApplication.ringCommands.dialog.PasswordDialogListener;
import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

/**
 * @author Alberto Ursino, Alessandra Tonin
 */
public class MainActivity extends AppCompatActivity implements PasswordDialogListener {

    private static final int PERMISSION_CODE = 0;
    private static final int APPLICATION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        SMSHandler smsHandler = SMSHandler.getInstance(context);

        Log.d("1", "pass stored: " + PasswordManager.getPassword(context));
        //Control if there's a password stored in memory, if not then open a dialog
        if (!PasswordManager.isPassSaved(context)) {
            openDialog();
        }

        final Ringtone RINGTONE = RingtoneHandler.getDefaultRingtone(context);
        final EditText PHONE_NUMBER = findViewById(R.id.telephoneNumber);
        final EditText SEND_PASSWORD = findViewById(R.id.password);
        final Button RING_BUTTON = findViewById(R.id.button);
        final Button STOP_BUTTON = findViewById(R.id.stop);

        //Request permissions to Receive SMS
        if (!SMSHandler.checkReceivePermission(context)) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSION_CODE);
        } else {
            smsHandler.setup(APPLICATION_CODE);
        }

        smsHandler.addReceivedMessageListener(new ReceivedMessageListener(context, RINGTONE));

        RING_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendRingCommand(PHONE_NUMBER.getText().toString(), SEND_PASSWORD.getText().toString());
                } catch (InvalidSMSMessageException e) {
                    e.printStackTrace();
                } catch (InvalidTelephoneNumberException e) {
                    e.printStackTrace();
                }
            }
        });

        STOP_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RingtoneHandler.stopRingtone(RINGTONE);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                SMSHandler.getInstance(getApplicationContext()).setup(APPLICATION_CODE);
            } else {
                finish();
                System.exit(0);
            }
        }
    }

    /**
     * @param destination telephone number of the receiver
     * @param password
     */
    private void sendRingCommand(String destination, String password) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        final RingCommand ringCommand = new RingCommand(new SMSPeer(destination), createPassword(password));
        AppManager.sendCommand(getApplicationContext(), ringCommand, new SMSSentListener() {
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
        return "_" + password;
    }

    /**
     * Create the dialog where the user must enter a valid password
     */
    private void openDialog() {
        PasswordDialog passwordDialog = new PasswordDialog();
        passwordDialog.show(getSupportFragmentManager(), "Device Password");
    }

    @Override
    public void applyText(String password) {
        PasswordManager.setPassword(getApplicationContext(), password);
        Toast.makeText(getApplicationContext(), "Password saved", Toast.LENGTH_SHORT).show();
    }
}
