package com.gruppo4.SMSApp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.SMSApp.ringCommands.AppManager;
import com.gruppo4.SMSApp.ringCommands.ReceivedMessageListener;
import com.gruppo4.SMSApp.ringCommands.RingCommand;
import com.gruppo4.sms.dataLink.SMSManager;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSMessageHandler;
import com.gruppo4.sms.dataLink.SMSPeer;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

/**
 * @author Gruppo 4
 */
public class MainActivity extends AppCompatActivity {

    Button ringButton;
    EditText phoneNumber;
    EditText password;
    SMSManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsManager = new SMSManager();
        smsManager.getInstance(getApplicationContext()).addReceivedMessageListener(new ReceivedMessageListener(getApplicationContext()));

        ringButton = findViewById(R.id.button);
        phoneNumber = findViewById(R.id.telephoneNumber);
        password = findViewById(R.id.password);

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

    private void sendRingCommand(String destination, String password) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        RingCommand ringCommand = new RingCommand(new SMSPeer(destination), password);
        AppManager.sendCommand(getApplicationContext(), ringCommand, new SMSSentListener() {
            @Override
            public void onSMSSent(SMSMessage message, SMSMessage.SentState sentState) {
                smsManager.sendMessage(message);
                Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
