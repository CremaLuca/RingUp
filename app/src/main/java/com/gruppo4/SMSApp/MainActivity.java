package com.gruppo4.SMSApp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.eis.smslibrary.SMSHandler;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
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
        SMSHandler.getInstance().setup(getApplicationContext());
        SMSHandler.getInstance().setReceivedListener(MessageReceivedService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityHelper.setState(MainActivityHelper.MainActivityState.ONCREATE);

        setContentView(R.layout.activity_main);

        SMSBackgroundHandler.onAppCreate(this);

        createNotificationChannel();

        setup();

        //Only if the activity is started by a service
        startFromService();

        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        sendButton = findViewById(R.id.test_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendTestMessage();
            }
        });

        if(!checkPermission()) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, 1);
        }else{
            Log.d("MainActivity", "You have the right permissions for this app");
        }
    }

    /**
     * Updates intent obtained from a service's call
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        startFromService();
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

    /**
     * Sends an SMS message to the inserted number
     *
     */
    public void sendTestMessage() {
        Log.d("MainActivity", "Sending test message");

        SMSHandler.getInstance().sendMessage(new SMSMessage(new SMSPeer(edtPhoneNumber.getText().toString()),"Test message"));
    }

    /**
     * Create the NotificationChannel, but only on API 26+ because
     * the NotificationChannel class is new and not in the support library
     *
     * Register the channel with the system; you can't change the importance
     * or other notification behaviors after this
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "TestChannelDescription";
            //IMPORTANCE_HIGH makes pop-up the notification
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Manages action from intent
     */
    private void startFromService() {
        Log.d("MainActivity","startFromService called");
        Intent intent = getIntent();
        if(intent != null) {
            switch(intent.getAction()) {
                case MessageReceivedService.ALERT_ACTION: {
                    createStopRingDialog();
                    Log.d("MainActivity","Creating StopRingDialog...");
                    break;
                }

                default:
                    break;
            }
        }
    }

    /**
     * Creates and shows AlertDialog with one option:
     * [stop] --> stop the ringtone and cancel the notification
     *
     */
    private void createStopRingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Your phone is ringing, stop it from here if you want");
        builder.setCancelable(true);
        Log.d("MainActivity","StopRingDialog created");

        builder.setPositiveButton(
                "Stop", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MessageReceivedService service = new MessageReceivedService();
                        service.stopAlarm();
                        Log.d("MainActivity","Stopping ringtone");
                        //cancel the right notification by id
                        int id = getIntent().getIntExtra(MessageReceivedService.NOTIFICATION_ID, -1);
                        NotificationManagerCompat.from(getApplicationContext()).cancel(id);
                        Log.d("MainActivity","Notification " + id + " cancelled");
                        dialogInterface.dismiss();
                    }
                }
        );

        AlertDialog alert = builder.create();
        alert.show();
        Log.d("MainActivity","Showing StopRingDialog...");
    }

    /**
     * @return true if the app has permissions, false otherwise
     */
    public boolean checkPermission() {
        if (!(getApplicationContext().checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) ||
                !(getApplicationContext().checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED))
            return false;
        else
            return true;
    }
}
