package com.gruppo4.SMSApp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;
import com.gruppo4.sms.SMSReceivedMessage;
import com.gruppo4.sms.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.listeners.SMSRecieveListener;
import com.gruppo4.sms.listeners.SMSSentListener;

public class MainActivity extends AppCompatActivity implements SMSRecieveListener, SMSSentListener {

    SMSController smsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Richiediamo il permesso di leggere i messaggi
        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);

        smsController = new SMSController(123);

        smsController.addOnReceiveListener(this);
        smsController.addOnSentListener(this);

        Button sendSmileButton = findViewById(R.id.sendSmileButton);
        sendSmileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendSmileButton();
            }
        });
    }

    public void onSendSmileButton(){
        String phoneNumber = ((AutoCompleteTextView)findViewById(R.id.phoneNumberTextView)).getText().toString();
        try {
            SMSMessage message = new SMSMessage(phoneNumber, "Smile!AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVDEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRAAAAAAAAAAAA",1);
            smsController.sendMessage(this, message);
        }catch(InvalidSMSMessageException messageException){
            Log.e("MainActivity",messageException.getMessage());
        }catch(InvalidTelephoneNumberException telephoneException){
            String error = "";
            switch (telephoneException.getState()) {
                case TELEPHONE_NUMBER_TOO_SHORT:
                    error = "Telephone number is too short!";
                    break;
                case TELEPHONE_NUMBER_TOO_LONG:
                    error="Telephone number is too long!";
                    break;
                case TELEPHONE_NUMBER_NO_COUNTRY_CODE:
                    error = "You have to insert the country code";
                    break;
                case TELEPHONE_NUMBER_NOT_A_NUMBER:
                    error = "The telephone number is not valid";
                    break;
            }
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            Log.e("MainActivity",telephoneException.getMessage());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberTextView = findViewById(R.id.number);
        smileButton = findViewById(R.id.smile_button);
        smileReceiver = findViewById(R.id.smileReceiver);
        textView = findViewById(R.id.textView);

        SMSController.addOnReceivedListener(this);

        smileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telephoneNumber = numberTextView.getText().toString();
                if (!SMSMessage.checkTelephoneNumber(telephoneNumber)) {
                    Toast.makeText(MainActivity.this, "Wrong telephone number", Toast.LENGTH_LONG).show();
                    return;
                }
                smsMessage = new SMSMessage(telephoneNumber, "Sent you a smile :)");
                SMSController.sendMessage(smsMessage, getBaseContext(), MainActivity.this);
            }
        });
    }

    @Override
    public void onSentReceived(SMSMessage message) {
        switch (message.getState()) {
            case MESSAGE_SENT:
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
                textView.setText("Message sent to " + smsMessage.getTelephoneNumber());
                break;
            default:
                Toast.makeText(this, "Message not sent", Toast.LENGTH_SHORT).show();
                textView.setText("Message not sent to " + smsMessage.getTelephoneNumber());
                break;
        }

    }

    @Override
    public void onMessageReceived(SMSMessage message) {
        smileReceiver.setText(message.getTelephoneNumber() + " " + message.getMessage());
    }

    @Override
    public void onSMSRecieve(SMSReceivedMessage message) {
        Toast.makeText(this,message.getTelephoneNumber() + " sent you a smile :)", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSMSSent(SMSController.SMSSentState state) {
        Log.d("MainActivity","Message sent");
        switch (state){
            case MESSAGE_SENT:
                Toast.makeText(this, "Smile sent :)", Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.w("MainActivity","Unable to send sms, reason:"+state);
                Toast.makeText(this, "Unable to send smile :(", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
