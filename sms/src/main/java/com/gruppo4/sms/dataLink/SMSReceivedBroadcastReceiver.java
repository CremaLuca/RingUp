package com.gruppo4.sms.dataLink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceivedBroadcastReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Log.v("SMSReceiver", "received message from android broadcaster");
        Bundle extras = intent.getExtras();
        if(extras != null){
            Object[] smsExtra = (Object[]) extras.get("pdus");
            String format = (String) extras.get("format");
            Log.v("SMSReceiver", "Extras length: " + smsExtra.length);
            for(int i = 0; i < smsExtra.length; ++i){
                SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i], format);
                String smsContent = sms.getMessageBody();
                String phoneNumber = sms.getOriginatingAddress();

                Toast.makeText(context, "Messaggio da " + phoneNumber, Toast.LENGTH_SHORT).show();

                SMSPacket packet = parsePacket(context, smsContent);
                if(packet != null)
                    SMSHandler.onReceive(context, packet, phoneNumber);
            }
        }
    }

    private SMSPacket parsePacket(Context context, String message) {
        SMSPacket packet = null;
        String[] splits = message.split(SMSPacket.SEPARATOR, 5);
        if (splits.length == 5) {
            try {
                int applicationCode = Integer.parseInt(splits[0]);
                int messageId = Integer.parseInt(splits[1]);
                int packetNumber = Integer.parseInt(splits[2]);
                int totalNumber = Integer.parseInt(splits[3]);
                String text = splits[4];
                if (applicationCode == SMSHandler.getApplicationCode(context)) {
                    packet = new SMSPacket(applicationCode, messageId, packetNumber, totalNumber, text);
                } else {
                    Log.v("SMSReceiver", "Received a message sent from this library, but not for this app id:" + SMSHandler.getApplicationCode(context));
                }
            } catch (NumberFormatException e) {
                Log.v("SMSReceiver", "Received a SMS, but not for this app");
            }
        }

        return packet;
    }
}
