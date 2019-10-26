package com.gruppo4.SMSApp;

import android.Manifest;
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
import com.gruppo4.sms.SMSReceivedMessage;
import com.gruppo4.sms.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.listeners.SMSReceivedListener;
import com.gruppo4.sms.listeners.SMSSentListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SMSReceivedListener, SMSSentListener {

    private RecyclerView recyclerView;
    private SmileAdapter mAdapter;

    private static final String SMILE_COMMAND = "SMILE_COMMAND";
    private static final int SMILE_MESSAGE_ID = 1;
    private static final int EXTRA_MESSAGE_ID = 543;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Permission to receive messages
        requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        //Permission to send messages
        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);

        ArrayList<String> smiles = new ArrayList<>();

        recyclerView = findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new SmileAdapter(smiles);
        recyclerView.setAdapter(mAdapter);

        SMSController.setup(this, 123);

        SMSController.addOnReceiveListener(this, SMILE_MESSAGE_ID);

        findViewById(R.id.sendSmileButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendSmileButton();
            }
        });
        findViewById(R.id.sendExtraCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendWrongCodeButton();
            }
        });
    }

    /**
     * Sends a message using the sms library
     *
     * @param text
     * @param telephoneNumber
     * @param messageCode
     */
    private void sendMessage(String text, String telephoneNumber, int messageCode) {
        try {
            SMSMessage message = new SMSMessage(telephoneNumber, text, messageCode);
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

    public void onSendWrongCodeButton() {
        String phoneNumber = ((AutoCompleteTextView) findViewById(R.id.phoneNumberTextView)).getText().toString();
        sendMessage(SMILE_COMMAND, phoneNumber, EXTRA_MESSAGE_ID);
    }

    /**
     * Callback for send smile button pressed. Sends a message to the number specified in the phoneNumberTextView
     */
    public void onSendSmileButton() {
        String phoneNumber = ((AutoCompleteTextView) findViewById(R.id.phoneNumberTextView)).getText().toString();
        sendMessage(SMILE_COMMAND, phoneNumber, SMILE_MESSAGE_ID);
    }

    @Override
    public void onSMSReceived(SMSReceivedMessage message) {
        if (message.getMessage().equals(SMILE_COMMAND)) {
            Log.d("MainActivity", "Received message:" + message.getMessage());
            Toast.makeText(this, message.getTelephoneNumber() + " sent you a smile :)", Toast.LENGTH_LONG).show();
            mAdapter.getSmiles().add(message.getTelephoneNumber() + " sent you a smile :)");
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSMSSent(SMSMessage message, SMSMessage.SentState state) {
        Log.d("MainActivity", "Message sent");
        if (state == SMSMessage.SentState.MESSAGE_SENT) {
            if (message.getMessageCode() == SMILE_MESSAGE_ID) {
                Toast.makeText(this, "Smile sent :)", Toast.LENGTH_SHORT).show();
                mAdapter.getSmiles().add("You sent a smile to " + message.getTelephoneNumber());
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
                mAdapter.getSmiles().add("You sent a message to " + message.getTelephoneNumber());
                mAdapter.notifyDataSetChanged();
            }
        } else {
            Log.w("MainActivity", "Unable to send sms, reason: " + state);
            if (message.getMessageCode() == SMILE_MESSAGE_ID)
                Toast.makeText(this, "Unable to send smile :(", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Unable to send message, reason: " + state, Toast.LENGTH_LONG).show();
        }
    }
}
