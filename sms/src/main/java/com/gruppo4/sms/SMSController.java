package com.gruppo4.sms;

import android.telephony.SmsManager;

public class SMSController {

    //costruttore
    public SMSController(){};

    //invio messaggio
    public void sendMessage(SMSMessage messaggio){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(messaggio.getNumber(), null, messaggio.getText(),null, null);

    }

}
