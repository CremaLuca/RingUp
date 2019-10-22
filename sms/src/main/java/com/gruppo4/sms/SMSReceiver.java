package com.gruppo4.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get("pdus");
        if (pdus != null) {
            for (int i = 0; i < pdus.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                SmsMessage message = android.telephony.SmsMessage.createFromPdu((byte[]) pdus[i], format);
                SMSMessage mess = new SMSMessage(message.getOriginatingAddress(), message.getMessageBody());
                SMSController.callReceivedListeners(mess);
            }
        }


    }
}
