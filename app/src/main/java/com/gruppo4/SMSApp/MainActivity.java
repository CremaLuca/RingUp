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

    /*
     *What happens when the app is opened
     *@param a Bundle
     *@return void
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //we ask for the permissions to send and receive sms
        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
        requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},1);

        //send a sms as the app is opened
        SMSController controller = new SMSController();
        SMSMessage sms = new SMSMessage("3457090735", "hello, this is a test sms");
        controller.sendMessage(sms);

        //this is a toast to notify the message's sending
        Context context = getApplicationContext();
        CharSequence text = "Message sent!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }
}
