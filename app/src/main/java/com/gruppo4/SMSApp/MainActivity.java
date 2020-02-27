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
 * MainActivity manages the front-end of RingUp
 * and lets the user send a test message to an inserted phone number
 *
 * Also manages:
 * creation of a notification when a message arrives
 * creation of a dialog box to stop the alarm
 * updating of this activity's state
 *
 * @author Luca Crema
 * @author Marco Tommasini
 * @author Alessandra Tonin
 */
public class MainActivity extends AppCompatActivity {

    public final static String CHANNEL_ID = "123";

    private EditText edtPhoneNumber;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initializing the state of the activity
        MainActivityHelper.setState(MainActivityHelper.MainActivityState.ONCREATE);
        setContentView(R.layout.activity_main);

        //Managing the background service
        SMSBackgroundHandler.onAppCreate(this);

        createNotificationChannel();
        setupHandler();


        //Checks if the activity was launched by a service
        //and manages that case
        startFromService();

        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        sendButton = findViewById(R.id.test_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendTestMessage();
            }
        });

        if(!checkPermission())
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, 1);
    }

    /**
     * Checks SEND_SMS and RECEIVE_SMS permissions
     * @return  true if the app has permissions, false otherwise
     */
    public boolean checkPermission() {
        if (!(getApplicationContext().checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) ||
                !(getApplicationContext().checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED))
            return false;
        else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Setup SMSHandler and initializes the receiver
     * which is needed to listen to new incoming messages
     */
    public void setupHandler() {
        SMSHandler.getInstance().setup(getApplicationContext());
        SMSHandler.getInstance().setReceivedListener(MessageReceivedService.class);
    }

    /**
     * Called only when this activity is launched with FLAG_ACTIVITY_SINGLE_TOP
     * because with that flag the activity is not re-launched but just showed (and then this method is called)
     *
     * Updates intent obtained from the service's call
     * @param intent    the vessel for all extras used to communicate from the service
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        startFromService();
    }

    /**
     * Checks if the activity was launched with an intent
     * Manages what to do from intent's action
     */
    private void startFromService() {
        Intent intent = getIntent();
        if(intent.getAction() != null) {
            switch(intent.getAction()) {
                case MessageReceivedService.ALERT_ACTION: {
                    createStopRingDialog();
                    break;
                }

                default:
                    break;
            }
        }
    }

    /**
     * Sends a test SMS message to the inserted phone number
     */
    public void sendTestMessage() {
        String phoneNumber = edtPhoneNumber.getText().toString();
        String testMessage = getResources().getString(R.string.test_message);
        SMSHandler.getInstance().sendMessage(new SMSMessage(new SMSPeer(phoneNumber),testMessage));
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

            String channelName = getResources().getString(R.string.channel_name);
            //IMPORTANCE_HIGH makes pop-up the notification
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);

            String channelDescription = getResources().getString(R.string.channel_description);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Creates and shows AlertDialog with two options:
     * [stop] --> stops the ringtone and cancels the notification
     * touch outside the dialog --> cancels the dialog and lets the ringtone ends by itself after a timeout
     */
    private void createStopRingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Your phone is ringing, stop it from here if you want");
        builder.setCancelable(true);

        builder.setPositiveButton(
                getResources().getString(R.string.stop), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new MessageReceivedService().stopAlarm();
                        //Cancel the right notification by id
                        int id = getIntent().getIntExtra(MessageReceivedService.NOTIFICATION_ID, -1);
                        if(id != -1)
                            NotificationManagerCompat.from(getApplicationContext()).cancel(id);
                        dialogInterface.dismiss();
                    }
                }
        );

        AlertDialog alert = builder.create();
        alert.show();
    }

    //region    Update MainActivityState
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
        //Starts the service
        SMSBackgroundHandler.onAppDestroy(this);
        MainActivityHelper.setState(MainActivityHelper.MainActivityState.ONDESTROY);
    }
    //endregion
}
