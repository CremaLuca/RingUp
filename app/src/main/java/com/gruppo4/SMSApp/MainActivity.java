package com.gruppo4.SMSApp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;
import com.gruppo4.sms.SMSReceivedListener;

public class MainActivity extends AppCompatActivity implements SMSReceivedListener {

    SMSMessage smsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Richiediamo il permesso di leggere e inviare i messaggi
        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
        requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},1);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SMSController.addOnReceivedListener(this);
        smsMessage = new SMSMessage("3460311953", "Ciao");
        SMSController.sendMessage(smsMessage, getBaseContext());
    }

    /*@Override
    public void onMessageSent(SMSMessage message, SMSController.SentStatus status) {
        switch (status) {
            case ERRORE:
                Toast.makeText(this, "Messaggio non inviato", Toast.LENGTH_SHORT).show();
                break;
            case INVIATO:
                Toast.makeText(this, "Messaggio inviato", Toast.LENGTH_SHORT).show();
                break;
        }

    }*/

    @Override
    public void onMessageReceived(SMSMessage message) {
        Toast.makeText(this, message.getTelephonNumber()+" ti ha inviato "+message.getMessage(), Toast.LENGTH_LONG).show();
    }
}
