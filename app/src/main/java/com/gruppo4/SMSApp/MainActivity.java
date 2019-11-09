package com.gruppo4.SMSApp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.SMSApp.ringCommands.AppManager;
import com.gruppo4.SMSApp.ringCommands.PasswordManager;
import com.gruppo4.SMSApp.ringCommands.ReceivedMessageListener;
import com.gruppo4.SMSApp.ringCommands.RingCommand;
import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

/**
 * @author Alberto Ursino
 */
public class MainActivity extends AppCompatActivity {

    Button ringButton;
    Button setPassword;
    EditText phoneNumber;
    EditText password;
    EditText defaultPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        new SMSHandler().setup(context, 1);

        SMSHandler.getInstance(getApplicationContext()).addReceivedMessageListener(new ReceivedMessageListener(context));

        ringButton = findViewById(R.id.button);
        phoneNumber = findViewById(R.id.telephoneNumber);
        password = findViewById(R.id.password);
        defaultPassword = findViewById(R.id.defaultPassword);
        setPassword = findViewById(R.id.setPassword);

        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordManager.setPassword(getApplicationContext(), defaultPassword.getText().toString());
                Toast.makeText(getApplicationContext(), "Password memorized", Toast.LENGTH_SHORT).show();
            }
        });

        ringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendRingCommand(phoneNumber.getText().toString(), password.getText().toString());
                } catch (InvalidSMSMessageException e) {
                    e.printStackTrace();
                } catch (InvalidTelephoneNumberException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param destination telephone number of the receiver
     * @param password    password
     * @throws InvalidSMSMessageException
     * @throws InvalidTelephoneNumberException
     */
    private void sendRingCommand(String destination, String password) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        RingCommand ringCommand = new RingCommand(new SMSPeer(destination), createPassword(password));
        AppManager.sendCommand(getApplicationContext(), ringCommand, new SMSSentListener() {
            @Override
            public void onSMSSent(SMSMessage message, SMSMessage.SentState sentState) {
                Toast.makeText(MainActivity.this, "Command sent", Toast.LENGTH_SHORT).show();
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
}
