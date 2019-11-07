package com.gruppo4.SMSApp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.sms.dataLink.SMSManager;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
        else
            sendTestMessage();
    }

    private void sendTestMessage() {
        try {
            SMSMessage message = new SMSMessage(456, new SMSPeer("+393467965447"), "Ciao!");
            SMSManager.getInstance(this).sendMessage(message, new SMSSentListener() {
                @Override
                public void onSMSSent(SMSMessage message, SMSMessage.SentState sentState) {
                    Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (InvalidTelephoneNumberException e) {
            Toast.makeText(this, "Wrong telephone number: " + e.getState(), Toast.LENGTH_SHORT).show();
        } catch (InvalidSMSMessageException e) {
            Toast.makeText(this, "Wrong message ? " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendTestMessage();
        }
    }


}
