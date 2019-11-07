package com.gruppo4.SMSApp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.sms.dataLink.SMSManager;

/**
 * @author Gruppo 4
 */
public class MainActivity extends AppCompatActivity {

    public void setup() {
        //Initialize the receiver
        SMSManager.getInstance(this).addReceivedMessageListener(new ReceivedMessageListener(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void sendRingCommand(String destination, String password) {
        //RingCommand command = new RingCommand(new SMSPeer(destination), password);
        //AppManager.sendCommand(this,command,new SMSSentListener(){})
        //TODO : Dentro al listener un toast per dire che il comando è stato inviato
    }

}
