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
import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

/**
 * @author Alberto Ursino, Alessandra Tonin
 */
public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 0;
    private static final int APPLICATION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        //Request permissions to Receive SMS
        if (!SMSHandler.checkReceivePermission(context)) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSION_CODE);
        } else {
            new SMSHandler().setup(context, APPLICATION_CODE);
        }

        final Ringtone ringtone = RingtoneHandler.getDefaultRingtone(context);
        final EditText phoneNumber = findViewById(R.id.telephoneNumber);
        final EditText password = findViewById(R.id.password);
        final EditText defaultPassword = findViewById(R.id.defaultPassword);
        final Button ringButton = findViewById(R.id.button);
        final Button setPassword = findViewById(R.id.setPassword);
        final Button stop = findViewById(R.id.stop);

        Log.d("1", "+++++++++++++++++++++++++++++++++getPassword: "+PasswordManager.getPassword(context));

        if(PasswordManager.getPassword(context) == null) {
            //Password must be memorize first
            ringButton.setEnabled(false);
            phoneNumber.setEnabled(false);
            password.setEnabled(false);
            stop.setEnabled(false);
        }

        SMSHandler.getInstance(getApplicationContext()).addReceivedMessageListener(new ReceivedMessageListener(context, ringtone));

        ringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordIsEmpty(password.getText().toString())) {
                    try {
                        sendRingCommand(phoneNumber.getText().toString(), password.getText().toString());
                    } catch (InvalidSMSMessageException e) {
                        e.printStackTrace();
                    } catch (InvalidTelephoneNumberException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Insert the password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RingtoneHandler.stopRingtone(ringtone);
            }
        });

        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordIsEmpty(defaultPassword.getText().toString())) {
                    PasswordManager.setPassword(getApplicationContext(), defaultPassword.getText().toString());
                    ringButton.setEnabled(true);
                    phoneNumber.setEnabled(true);
                    password.setEnabled(true);
                    stop.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Password memorized", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Create a not empty password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
     * Check if the password is empty, true = yes, false = no
     */
    private boolean passwordIsEmpty(String password) {
        return password.equals("");
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                new SMSHandler().setup(getApplicationContext(), APPLICATION_CODE);
            } else {
                finish();
                System.exit(0);
            }
        }
    }
}
