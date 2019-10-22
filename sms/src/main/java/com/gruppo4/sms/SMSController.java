package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;

public class SMSController {

    public enum SentStatus{
        INVIATO,
        ERRORE
    }

    public static ArrayList<SMSReceivedListener> receivedListeners = new ArrayList<>();

    public SMSController(){}

    //Invio del messaggio
    public static void sendMessage(SMSMessage message, Context context) {

        SmsManager sms = SmsManager.getDefault();
        String SENT = "SMS_SENT";

        PendingIntent sentPI = PendingIntent.getBroadcast(context , 0 ,new Intent(SENT), 0);

        sms.sendTextMessage(message.getTelephonNumber(), null, message.getMessage(), sentPI, null);

    }

    public static void addOnReceivedListener(SMSReceivedListener smsReceivedListener){
        receivedListeners.add(smsReceivedListener);
    }

    public static void callReceivedListener(SMSMessage message){
        for(SMSReceivedListener listener: receivedListeners) {
            listener.onMessageReceived(message);
        }
    }
}