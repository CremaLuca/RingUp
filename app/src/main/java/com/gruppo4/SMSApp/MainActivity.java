package com.gruppo4.SMSApp;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;

/**
 * @author Alessandra Tonin
 *
 * CODE REVIEW FOR VELLUDO AND TURCATO
 */

public class MainActivity extends AppCompatActivity {

    final String myPhoneNumber = "5555215554";
    final String myHelloText = "Hello, this is a test sms";
    final String mySentFeedback = "Message sent!!";

    /**
     * What happens when the app is opened
     *
     * @param savedInstanceState a Bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //we ask for the permissions to send and receive sms
        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
        requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1);

        //send a sms as the app is opened
        SMSController controller = new SMSController();
        SMSMessage sms = new SMSMessage(myPhoneNumber, myHelloText);
        controller.sendMessage(sms);

        //this is a toast to notify the message's sending
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, mySentFeedback, duration);
        toast.show();

    }
}
