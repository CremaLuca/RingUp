package com.gruppo4.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * @author Tommasini Marco
 */

public class SMSReceiver extends BroadcastReceiver {

    /**
     * Called when a message arrives, calls onReceived in SMSController
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("SMSReceiver", "received message from android broadcaster");
        Bundle extras = intent.getExtras();
        if(extras != null){
            Object[] smsExtra = (Object[]) extras.get("pdus");
            String format = (String) extras.get("format");
            Log.v("SMSReceiver", "Extras length: " + smsExtra.length);
            for(int i = 0; i < smsExtra.length; ++i){
                SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i], format);
                String body = sms.getMessageBody();
                String phoneNumber = sms.getOriginatingAddress();

                SMSController.listener.onReceived(new SMSMessage(body, phoneNumber));
            }
        }
    }
}
