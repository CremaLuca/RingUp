package com.gruppo4.SMSApp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;

/**
 * @author Gruppo 4
 */
public class MainActivity extends AppCompatActivity {

    public void setup() {
        //Initialize the receiver
        SMSHandler.getInstance(this).setup(123);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start a service that keeps the ActivityHelperReceiver alive
        Intent activityHelperIntent = new Intent(getApplicationContext(), ActivityHelperService.class);
        startService(activityHelperIntent);

        setup();

        if(!SMSHandler.checkPermissions(this)) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, 1);
        }else{

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void sendTestMessage(){
        SMSHandler.getInstance(this).sendMessage(new SMSMessage(123,new SMSPeer("+393467965447"),"Test message"));
    }

}
