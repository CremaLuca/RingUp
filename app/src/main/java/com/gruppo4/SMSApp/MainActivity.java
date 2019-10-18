package com.gruppo4.SMSApp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import android.telephony.SmsManager;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;
import com.gruppo4.sms.interfaces.SMSReceivedListener;
import com.gruppo4.sms.interfaces.SMSSentListener;

public class MainActivity extends AppCompatActivity implements SMSReceivedListener, SMSSentListener {

    SMSMessage smsMessage;
    TextView numberTextView, smileReceiver, textView;
    Button smileButton;

    private static final String SENT = "SMS_SENT";

    BroadcastReceiver sentActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            Log.d("MainActivity","WEE ABBIAMO MANDATO UN MESSAGGIO");
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "SMS delivered",
                            Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getBaseContext(), "SMS not delivered GENERIC FAILURE",
                            Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(getBaseContext(), "SMS not delivered RADIO OFF",
                            Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getBaseContext(), "SMS not delivered NULL PDU",
                        Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Richiediamo il permesso di leggere i messaggi
        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

        registerReceiver(sentActivity, new IntentFilter(SENT));

        SMSController controller = new SMSController();
        controller.sendMessage("+393467965447","Ciao Luca",sentPI);

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
