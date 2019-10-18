package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.telephony.SmsManager;

public class SMSController {

    public static void sendMessage(String telephoneNumber, String text, PendingIntent onSent){
        SmsManager.getDefault().sendTextMessage(telephoneNumber,null,text,onSent,null);
    }

    public static void addOnReceiveListener(Context context){

    }

}
