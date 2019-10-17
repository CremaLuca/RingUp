package com.gruppo4.sms;

import android.telephony.SmsManager;

public class SMSController {

    public void sendMessage(String telephoneNumber, String text){

        SmsManager.getDefault().sendTextMessage(telephoneNumber,null,text,null,null);

    }

}
