package com.gruppo4.sms.dataLink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;

public class SMSReceivedBroadcastReceiver extends BroadcastReceiver {

    public static SMSReceivedListener listener;

    /*public void setReceivedistener(SMSReceivedListener listener) {
        this.listener = listener;
    }*/

    public void onReceive(Context context, Intent intent) {
        Log.d("SMSReceiver", "Received message from android broadcaster");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Object[] smsExtra = (Object[]) extras.get("pdus");
            String format = (String) extras.get("format");
            Log.v("SMSReceiver", "Extras length: " + smsExtra.length);
            for (int i = 0; i < smsExtra.length; i++) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i], format);
                String smsContent = sms.getMessageBody();
                String phoneNumber = sms.getOriginatingAddress();

                SMSMessage message = SMSMessageHandler.getInstance().parseMessage(smsContent, phoneNumber);
                if (message != null && message.getApplicationID() == SMSHandler.getInstance(context).getApplicationCode()) {
                    if(listener != null) {
                        listener.onMessageReceived(message, context);
                    } else {
                        Log.d("SMSReceiver", "Listener is null...");
                    }
                }
            }
        }
    }
}
