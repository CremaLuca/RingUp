package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;

public class SMSController {

    private static ArrayList<SMSReceivedListener> receivedListeners = new ArrayList<>();
    private static ArrayList<SMSSentListener> sentListeners = new ArrayList<>();


    /*
     *constructor
     */
    public SMSController(){};

    /*
     *send a SMS message
     *@param an object of SMSMessage class
     *@return void
     */
    public void sendMessage(SMSMessage message){

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(message.getPhoneNumber(), null, message.getTextMessage(),null, null);

    }

    /*
     *add a listener to the list receivedListeners
     *@param an object of SMSReceivedListener class
     *@return void
     */
    //NB: su Trello si chiama onMessageReceived(SMSReceivedListener listener)
    public static void addOnReceivedListeners(SMSReceivedListener listener){
        receivedListeners.add(listener);
    }

    /*
     *add a listener to the list sentListeners
     *@param an object of SMSSentListener class
     *@return void
     */
    //NB: su Trello si chiama onMessageSent(SMSSentListener listener)
    public static void addOnSentListeners(SMSSentListener listener){
        sentListeners.add(listener);
    }

    /*
     *When a message arrives, call the receivedListener
     *@param a SMSMessage object
     *@return void
     */
    public static void callReceivedListeners(SMSMessage message){
        for (SMSReceivedListener listener : receivedListeners){
            listener.onMessageReceived(message);
        }
    }

}
