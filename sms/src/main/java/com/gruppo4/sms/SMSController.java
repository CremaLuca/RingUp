package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;

public class SMSController {

    private static ArrayList<SMSReceivedListener> receivedListeners = new ArrayList<>();
    private static ArrayList<SMSSentListener> sentListeners = new ArrayList<>();

    //costruttore
    public SMSController(){};

    /*
     *invio messaggio
     *@param un oggetto SMSMessage
     *@return void
     */
    public void sendMessage(SMSMessage message){

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(message.getTelephoneNumber(), null, message.getTextMessage(),null, null);

    }

    //aggiunge alla lista di receivedListeners
    //NB: su Trello si chiama onMessageReceived(SMSReceivedListener listener)
    public static void addOnReceivedListeners(SMSReceivedListener listener){
        receivedListeners.add(listener);
    }

    //aggiunge alla lista di sentListeners
    //NB: su Trello si chiama onMessageSent(SMSSentListener listener)
    public static void addOnSentListeners(SMSSentListener listener){
        sentListeners.add(listener);
    }

    //quando arriva un messaggio chiamo il receivedListener
    public static void callReceivedListeners(SMSMessage messaggio){
        for (SMSReceivedListener listener : receivedListeners){
            listener.onMessageReceived(messaggio);
        }
    }

}
