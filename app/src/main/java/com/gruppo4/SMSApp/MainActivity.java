package com.gruppo4.SMSApp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.communication.dataLink.Peer;
import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;
import com.gruppo4.sms.dataLink.background.SMSBackgroundHandler;

/**
 * @author Gruppo 4
 */
public class MainActivity extends AppCompatActivity {

    private final static String CHANNEL_ID = "123";
    private EditText edtPhoneNumber;
    private Button sendButton;

    public void setup() {
        //Initialize the receiver
        SMSHandler.getInstance(this).setup(123);
        SMSHandler.getInstance(this).setReceivedMessageListener(MessageReceivedService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SMSBackgroundHandler.onAppCreate(this);

        createNotificationChannel();

        setup();

        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        sendButton = findViewById(R.id.test_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendTestMessage();
            }
        });

        if(!SMSHandler.checkPermissions(this)) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, 1);
        }else{
            Log.d("MainActivity", "You have the right permissions for this app");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSBackgroundHandler.onAppDestroy(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public void sendTestMessage() {
        Log.d("MainActivity", "Sending test message");

        SMSPeer peer;
        if(edtPhoneNumber.getText().length() != 0)
            peer = new SMSPeer(edtPhoneNumber.getText().toString());
        else
            peer = new SMSPeer("+15555215556");

        SMSHandler.getInstance(this).sendMessage(new SMSMessage(123,peer.getAddress(),"Test message"));
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TestChannelName";
            String description = "TestChannelDescription";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
