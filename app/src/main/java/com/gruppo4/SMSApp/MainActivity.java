package com.gruppo4.SMSApp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;
import com.gruppo4.sms.SMSReceivedListener;
import com.gruppo4.sms.SMSSentListener;

public class MainActivity extends AppCompatActivity implements SMSReceivedListener, SMSSentListener {

    SMSMessage smsMessage;
    TextView number, smileReceiver, textView;
    Button smile_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number = findViewById(R.id.number);
        smile_button = findViewById(R.id.smile_button);
        smileReceiver = findViewById(R.id.smileReceiver);
        textView = findViewById(R.id.textView);

        SMSController.addOnReceivedListener(this);

        smile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsMessage = new SMSMessage(number.getText().toString(), "Ti ha inviato un sorriso :)");
                //Simple check to see if there's a number
                if(!(smsMessage.getTelephoneNumber().equals(""))) {
                    SMSController.sendMessage(smsMessage, getBaseContext());
                    textView.setText("Message sent to "+ smsMessage.getTelephoneNumber());
                }
                else
                    Toast.makeText(MainActivity.this, "Inserire numero", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSentReceived(SMSMessage message, SMSController.SentStatus status) {
        switch (status) {
            case ERROR:
                Toast.makeText(this, "Messaggio non inviato", Toast.LENGTH_SHORT).show();
                break;
            case SENT:
                Toast.makeText(this, "Messaggio inviato", Toast.LENGTH_SHORT).show();
                break;
        }
    }



    @Override
    public void onMessageReceived(SMSMessage message) {
        smileReceiver.setText(message.getTelephoneNumber()+" "+message.getMessage());
    }
}
