package com.gruppo4.SMSApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.gruppo4.sms.dataLink.SMSMessage;

/**
 * @author Gruppo 4
 */

public class ActivityHelperReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("SMSApp")) {

            SMSMessage message = (SMSMessage) intent.getSerializableExtra("Message");

            Log.d("ActivityHelperReceiver", "Messaggio ricevuto da " + message.getPeer().getAddress());
            Toast.makeText(context, "Messaggio ricevuto da " + message.getPeer().getAddress(), Toast.LENGTH_SHORT).show();
        }
    }
}
