package com.gruppo4.SMSApp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

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
        SMSHandler.getInstance(this).setReceivedMessageListener(ActivityHelper.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        setup();

        if(!SMSHandler.checkPermissions(this)) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, 1);
        }else{
            Log.d("MainActivity", "You have the right permissions");
            sendTestMessage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        sendTestMessage();

    }

    private void sendTestMessage(){
        Log.d("MainActivity", "Sending test message");
        SMSHandler.getInstance(this).sendMessage(new SMSMessage(123,new SMSPeer("+393467965447"),"Test message"));
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TestChannelName";
            String description = "TestChannelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
