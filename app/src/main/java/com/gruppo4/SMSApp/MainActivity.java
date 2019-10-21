package com.gruppo4.SMSApp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;

public class MainActivity extends AppCompatActivity {

    SMSController smsController = new SMSController();
    SMSMessage smsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Richiediamo il permesso di leggere i messaggi
        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsMessage = new SMSMessage("NUMERO DI TELEFONO", "MESSAGGIO");
        smsController.sendMessage(smsMessage, getBaseContext());
    }
}
