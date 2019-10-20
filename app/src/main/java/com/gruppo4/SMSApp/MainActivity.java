package com.gruppo4.SMSApp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.gruppo4.sms.DestinationAddressException;
import com.gruppo4.sms.MsgBodyException;
import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;
import com.gruppo4.sms.SMSSendListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Richiediamo il permesso di leggere i messaggi
        requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SMSController controller = new SMSController();
        controller.listener = new SMSSendListener() {
            @Override
            public void onDelivered(SMSMessage message, SMSController.SMSState state) {

            }

            @Override
            public void onSent(SMSMessage message, SMSController.SMSState state) {

            }
        };
        SMSMessage sms = new SMSMessage("prova", "3464790892");
        controller.sendMessage(this, sms);

    }
}
