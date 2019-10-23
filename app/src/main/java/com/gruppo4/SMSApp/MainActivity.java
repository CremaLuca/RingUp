package com.gruppo4.SMSApp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;
import com.gruppo4.sms.interfaces.SMSReceivedListener;
import com.gruppo4.sms.interfaces.SMSSentListener;

public class MainActivity extends AppCompatActivity implements SMSReceivedListener, SMSSentListener {

    SMSMessage smsMessage;
    TextView numberTextView, smileReceiver, textView;
    Button smileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
}
