package com.gruppo4.SMSApp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.gruppo4.communication.dataLink.Peer;
import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;
import com.gruppo4.sms.dataLink.background.SMSBackgroundHandler;

/**
 * @author Gruppo 4
 */
public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_NAME = "TestChannelName";
    public static final String CHANNEL_ID = "123";

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

        MainActivityHelper.setState(MainActivityHelper.MainActivityState.ONCREATE);

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

    //not working
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            switch(data.getAction()) {
                case MainActivityHelper.START_ACTIVITY_RING: {
                    int id = data.getIntExtra(MessageReceivedService.NOTIFICATION_ID, -1);
                    createRingAlertDialog(id);
                    break;
                }

                default:
                    break;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent != null) {
            switch(intent.getAction()) {
                case MessageReceivedService.OPEN_ACTION: {
                    int id = intent.getIntExtra(MessageReceivedService.NOTIFICATION_ID, -1);
                    Toast.makeText(this, "aperta", Toast.LENGTH_SHORT).show();
                    //createRingAlertDialog(id);
                    break;
                }

                default:
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MainActivityHelper.setState(MainActivityHelper.MainActivityState.ONSTART);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivityHelper.setState(MainActivityHelper.MainActivityState.ONSTOP);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivityHelper.setState(MainActivityHelper.MainActivityState.ONPAUSE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivityHelper.setState(MainActivityHelper.MainActivityState.ONRESUME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSBackgroundHandler.onAppDestroy(this);
        MainActivityHelper.setState(MainActivityHelper.MainActivityState.ONDESTROY);
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
            String description = "TestChannelDescription";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createRingAlertDialog(final int notification_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your phone is ringing, stop it from here if you want");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Stop", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MessageReceivedService service = new MessageReceivedService();
                        service.stopAlarm();
                        if(notification_id != -1) {
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                            notificationManager.cancel(notification_id);
                        }
                        dialogInterface.cancel();
                    }
                }
        );
        builder.setNegativeButton(
                "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }
        );
        builder.show();
    }
}
