package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SMSController {

    enum SentStatus{

    }

    public SMSController(){}

    //Invio del messaggio
    public void sendMessage(SMSMessage message, Context context) {

        SmsManager sms = SmsManager.getDefault();
        String SENT = "SMS_SENT";

        PendingIntent sentPI = PendingIntent.getBroadcast(context , 0 ,new Intent(SENT), 0);

        sms.sendTextMessage(message.getTelephonNumber(), null, message.getMessage(), sentPI, null);

    }

    public void onMessageReceived(SMSReceivedListener listener) {


    }

    public void onMessageSent(SMSSentListener listener){



    }

}