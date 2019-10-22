package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;

public class SMSController {

    private static ArrayList<SMSReceivedListener> receivedListeners = new ArrayList<>();

    //costruttore
    public SMSController(){};

    /*
     *invio messaggio
     *@param un oggetto SMSMessage
     *@return void
     */
    public void sendMessage(SMSMessage messaggio){

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(messaggio.getNumber(), null, messaggio.getText(),null, null);

    }

    //aggiunge alla lista di receivedListeners
    public static void addOnReceivedListeners(SMSReceivedListener listener){
        receivedListeners.add(listener);
    }

    //quando arriva un messaggio chiamo il receivedListener
    public static void callReceivedListeners(SMSMessage messaggio){
        for (SMSReceivedListener listener : receivedListeners){
            listener.onMessageReceived(messaggio);
        }
    }

}
