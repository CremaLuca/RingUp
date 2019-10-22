package com.gruppo4.SMSApp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Richiediamo il permesso di leggere e inviare i messaggi
        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
        requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},1);

        //invia un sms all'apertura dell'app
        SMSController contr = new SMSController();
        SMSMessage sms = new SMSMessage("3457090735", "prova sms con classe SMSMessage");
        contr.sendMessage(sms);

        //toast per notificare l'invio del messaggio
        Context context = getApplicationContext();
        CharSequence text = "Messaggio inviato!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }
}
