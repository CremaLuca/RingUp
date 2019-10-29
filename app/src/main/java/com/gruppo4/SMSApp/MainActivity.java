package com.gruppo4.SMSApp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;
import com.gruppo4.sms.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.listeners.SMSReceivedListener;
import com.gruppo4.sms.listeners.SMSSentListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SMSReceivedListener, SMSSentListener {

    private static final String SMILE_COMMAND = "SMILE_COMMAND";
    private static final String HEART_COMMAND = "HEART_COMMAND";
    private static final int SMS_PERMISSION_CODE = 1;
    private RecyclerView listView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        //Permission to receive messages
        requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        //Permission to send messages
        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
=======
>>>>>>> marcom

        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        } else {
            setupSMSController();
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
                onSendSmileButton();
            }
        });
        findViewById(R.id.sendHeartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendHeartButton();
            }
        });
    }

    private void setupSMSController() {
        SMSController.init(getApplicationContext(), 123);

        SMSController.addOnReceiveListener(this);
    }

    /**
     * Sends a message using the sms library
     *
     * @param text
     * @param telephoneNumber
     */
    private void sendMessage(String text, String telephoneNumber) {
        try {
            SMSMessage message = new SMSMessage(telephoneNumber, text);
            SMSController.sendMessage(message, this);
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
            Log.e("MainActivity", telephoneException.getMessage());
        }
    }

    public void onSendHeartButton() {
        String phoneNumber = ((AutoCompleteTextView) findViewById(R.id.phoneNumberTextView)).getText().toString();
        sendMessage(HEART_COMMAND, phoneNumber);
    }

    /**
     * Callback for send smile button pressed. Sends a message to the number specified in the phoneNumberTextView
     */
    public void onSendSmileButton() {
        String phoneNumber = ((AutoCompleteTextView) findViewById(R.id.phoneNumberTextView)).getText().toString();
        sendMessage(SMILE_COMMAND, phoneNumber);
    }

    @Override
    public void onSMSReceived(SMSMessage message) {
        Log.d("DEBUG/mainactivity", "Received message:" + message.getMessage());
        if (message.getMessage().equals(SMILE_COMMAND)) {
            Log.d("DEBUG/mainactivity", "Received message:" + message.getMessage());
            adapter.getEvents().add(message.getTelephoneNumber() + " sent you a smile :)");
            adapter.notifyDataSetChanged();
        } else if (message.getMessage().equals(HEART_COMMAND)) {
            Log.d("DEBUG/mainactivity", "Received message:" + message.getMessage());
            adapter.getEvents().add(message.getTelephoneNumber() + " sent you a heart <3");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSMSSent(SMSMessage message, SMSController.SentState state) {
        Log.d("MainActivity", "Message sent");
        if (state == SMSController.SentState.MESSAGE_SENT) {
            if (message.getMessage().equals(SMILE_COMMAND)) {
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
                adapter.getEvents().add("You sent a :) to " + message.getTelephoneNumber());
                adapter.notifyDataSetChanged();
            } else if (message.getMessage().equals(HEART_COMMAND)) {
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
                adapter.getEvents().add("You sent a <3 to " + message.getTelephoneNumber());
                adapter.notifyDataSetChanged();
            }
        } else {
            Log.w("DEBUG/mainactivity", "Unable to send sms, reason: " + state);
            Toast.makeText(this, "Unable to send message, reason: " + state, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SMS_PERMISSION_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay!
                setupSMSController();
            } else {
                // permission denied, boo!
                // close the app then
                finish();
                System.exit(0);
            }
        }
    }
}
