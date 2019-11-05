package com.gruppo4.SMSApp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gruppo4.sms.dataLink.SMSManager;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SMSReceivedListener, SMSSentListener {

    private static final String SMILE_COMMAND = "SMILE_COMMAND";
    private static final String HEART_COMMAND = "HEART_COMMAND";
    private static final String LONG_COMMAND_PREFIX = "LONG_COMMAND";
    private static final int APP_ID = 123;
    private static final String LONG_COMMAND = LONG_COMMAND_PREFIX + " This command is way too long to be sent in one single sms, this takes at least two or three sms to be completely sent. " +
            "And to prove it i can just send you this";
    private static final int SMS_RECEIVE_PERMISSION_CODE = 1;
    private static final int SMS_SEND_PERMISSION_CODE = 2;
    private RecyclerView listView;
    private ListAdapter adapter;
    private static int mark = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //We need receive message permission, if we don't have it, we can't send messages
        if (!checkRecevieSMSPermission()) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, SMS_RECEIVE_PERMISSION_CODE);
        } else {
            //We can be setup for reception
            setupSMSManager();
        }

        ArrayList<String> events = new ArrayList<>();

        listView = findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);

        adapter = new ListAdapter(events);
        listView.setAdapter(adapter);

        findViewById(R.id.sendSmileButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonSendMessage(getApplicationContext(), SMILE_COMMAND, 1);
            }
        });
        findViewById(R.id.sendHeartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonSendMessage(getApplicationContext(), HEART_COMMAND, 2);
            }
        });
        findViewById(R.id.sendLongButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonSendMessage(getApplicationContext(), LONG_COMMAND, 3);
            }
        });

    }

    private boolean checkRecevieSMSPermission() {
        return SMSManager.checkReceivePermission(getApplicationContext());
    }

    private boolean checkSendSMSPermission() {
        return SMSManager.checkSendPermission(getApplicationContext());
    }

    private void setupSMSManager() {
        Context ctx = getApplicationContext();
        if (!SMSManager.getInstance(ctx).isSetup()) {
            SMSManager.getInstance(ctx).setup(APP_ID);
        }
        SMSManager.getInstance(ctx).addReceivedMessageListener(this);
    }

    /**
     * Sends a message using the sms library
     *
     * @param text the content of the message
     * @param telephoneNumber the target telephone number
     */
    private void sendMessage(Context context, String text, String telephoneNumber) {
        try {
            SMSMessage message = new SMSMessage(context, new SMSPeer(telephoneNumber), text);
            SMSManager.getInstance(context).sendMessage(message, this);
        } catch (InvalidSMSMessageException messageException) {
            Log.e("MainActivity", messageException.getMessage());
        } catch (InvalidTelephoneNumberException telephoneException) {
            String error = "";
            switch (telephoneException.getState()) {
                case TELEPHONE_NUMBER_TOO_SHORT:
                    error = "Telephone number is too short!";
                    break;
                case TELEPHONE_NUMBER_TOO_LONG:
                    error = "Telephone number is too long!";
                    break;
                case TELEPHONE_NUMBER_NO_COUNTRY_CODE:
                    error = "You have to insert the country code";
                    break;
                case TELEPHONE_NUMBER_NOT_A_NUMBER:
                    error = "The telephone number is not valid";
                    break;
            }
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            Log.e("MainActivity", telephoneException.getMessage() + ", shown error: " + error);
        }
    }

    void onButtonSendMessage(Context ctx, String message, int internalCode) {
        mark = internalCode;
        if (!SMSManager.checkPermissions(ctx)) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SMS_RECEIVE_PERMISSION_CODE);
        } else {
            String phoneNumber = ((EditText) findViewById(R.id.phoneNumberTextView)).getText().toString();
            sendMessage(ctx, message, phoneNumber);
        }
    }

    @Override
    public void onMessageReceived(SMSMessage message) {
        Log.d("MainActivity", "Received message:" + message.getData());
        if (message.getData().equals(SMILE_COMMAND)) {
            adapter.getEvents().add(message.getPeer().getAddress() + " sent you a smile :)");

        } else if (message.getData().equals(HEART_COMMAND)) {
            adapter.getEvents().add(message.getPeer().getAddress() + " sent you a heart <3");
        } else if (message.getData().startsWith(LONG_COMMAND_PREFIX)) {
            adapter.getEvents().add(message.getPeer().getAddress() + " sent you a looong command");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSMSSent(SMSMessage message, SMSMessage.SentState state) {
        Log.d("MainActivity", "Message sent: " + message.getData());
        if (state == SMSMessage.SentState.MESSAGE_SENT) {
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
            if (message.getData().equals(SMILE_COMMAND)) {
                adapter.getEvents().add("You sent a :) to " + message.getPeer().getAddress());
            } else if (message.getData().equals(HEART_COMMAND)) {
                adapter.getEvents().add("You sent a <3 to " + message.getPeer().getAddress());
            } else if (message.getData().startsWith(LONG_COMMAND_PREFIX)) {
                adapter.getEvents().add("You sent a looong command to " + message.getPeer().getAddress());
            }
            adapter.notifyDataSetChanged();
        } else {
            Log.w("MainActivity", "Unable to send sms, reason: " + state);
            Toast.makeText(this, "Unable to send message, reason: " + state, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public synchronized void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Context ctx = getApplicationContext();
        if (requestCode == SMS_RECEIVE_PERMISSION_CODE) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupSMSManager();
                Toast.makeText(ctx, "Grazie per i permessi :)", Toast.LENGTH_SHORT).show();
            } else {
                //Close the app, can't work without permission
                finish();
                System.exit(0);
            }
        }
        if (requestCode == SMS_SEND_PERMISSION_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay!
                String phoneNumber = ((EditText) findViewById(R.id.phoneNumberTextView)).getText().toString();
                switch (mark) {
                    case 1:
                        sendMessage(ctx, SMILE_COMMAND, phoneNumber);
                        break;
                    case 2:
                        sendMessage(ctx, HEART_COMMAND, phoneNumber);
                        break;
                    case 3:
                        sendMessage(ctx, LONG_COMMAND, phoneNumber);
                        break;
                }
            } else {
                //Nothing, we just won't send the message
                Toast.makeText(ctx, "Unable to send the message, missing permissions :(", Toast.LENGTH_LONG).show();
            }
        }
    }
}
