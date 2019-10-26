package com.gruppo4.sms.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSPacket;

public class SMSReceivedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null){
            Object[] smsExtra = (Object[]) extras.get("pdus");
            String format = (String) extras.get("format");
            for(int i=0;i<smsExtra.length;++i){
                SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i], format);
                String text = sms.getMessageBody();
                String number = sms.getOriginatingAddress();
                //Packet building
                SMSPacket packet = SMSPacket.parseSMSPacket(text);
                SMSController.onReceive(packet, number);
                abortBroadcast(); //Prevent the message from reaching the main SMS application
            }
        }
    }
}
