package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.gruppo4.sms.listeners.SMSRecieveListener;
import com.gruppo4.sms.listeners.SMSSentListener;

import java.util.ArrayList;

public class SMSController{

    /**
     * List of recieve listeners that are triggered on message sent
     */
    private ArrayList<SMSSentListener> onSentListeners;

    /**
     * List of recieve listeners that are triggered on message recieved
     */
    private ArrayList<SMSRecieveListener> onRecieveListeners;

    /**
     * SINGLETON
     */
    private static SMSController instance;

    public enum SMSSentState{
        MESSAGE_SENT,
        ERROR_GENERIC_FAILURE,
        ERROR_RADIO_OFF,
        ERROR_NULL_PDU,
        ERROR_NO_SERVICE,
        ERROR_LIMIT_EXCEEDED
    }

    public SMSController() {
        onRecieveListeners = new ArrayList<>();
    }

    public static void sendMessage(Context context, SMSMessage message){
        //Create a PendingIntent, when the message will be sent from the android SMSManager a beacon of SMS_SENT will be intercepted by our SMSSender class
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
        SmsManager.getDefault().sendTextMessage(message.getTelephoneNumber(),null,message.getMessageText(), sentPI,null);
    }

    public static void addOnSentListener(SMSSentListener listener){
        getInstance().onSentListeners.add(listener);
    }

    public static void addOnReceiveListener(SMSRecieveListener listener){
        getInstance().onRecieveListeners.add(listener);
    }

    /**
     * Method used by SMSSender to trigger every listener on message sent
     * @param state
     */
    protected static void onSent(SMSSentState state){
        //Foreach listener call its method.
        for(SMSSentListener listener : getInstance().onSentListeners){
            listener.onSMSSent(state);
        }
    }

    /**
     * Method used by SMSReceiver to trigger every listener on message recieved
     * @param message
     */
    protected static void onReceive(SMSMessage message){
        //Foreach listener call its method.
        for(SMSRecieveListener listener : getInstance().onRecieveListeners){
            listener.onSMSRecieve(message);
        }
    }

    protected static SMSController getInstance(){
        if(instance == null)
            instance = new SMSController();
        return instance;
    }

}
