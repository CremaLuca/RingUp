package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.gruppo4.sms.listeners.SMSRecieveListener;
import com.gruppo4.sms.listeners.SMSSentListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SMSController{

    /**
     * List of recieve listeners that are triggered on message sent
     */
    private ArrayList<SMSSentListener> onSentListeners;

    /**
     * List of recieve listeners that are triggered on message received
     */
    private ArrayList<SMSRecieveListener> onReceiveListeners;

    private int applicationCode;

    /**
     * List of incomplete messages received, when every packet of a message is arrived it gets removed from this list
     */
    private ArrayList<SMSReceivedMessage> receivedMessages;

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

    public SMSController(int applicationCode) {
        onReceiveListeners = new ArrayList<>();
        onSentListeners = new ArrayList<>();
        receivedMessages = new ArrayList<>();
        this.applicationCode = applicationCode;

        instance = this;
    }

    /**
     * Send a single SMS message
     * @param context
     * @param message
     */
    public void sendMessage(Context context, SMSMessage message){
        //Create a PendingIntent, when the message will be sent from the android SMSManager a beacon of SMS_SENT will be intercepted by our SMSSender class
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
        SmsManager smsManager = SmsManager.getDefault();
        SMSPacket[] packets = message.getPackets();

        ArrayList<String> textMessages = new ArrayList<>();
        ArrayList<PendingIntent> onSentIntents = new ArrayList<>();

        for (SMSPacket packet: packets) {
            textMessages.add(packet.getSMSOutput());
            onSentIntents.add(null);
            Log.d("SMSController", "Packet:" + packet.getSMSOutput());
        }
        //Except for the last one that will be a real callback
        onSentIntents.set(onSentIntents.size() - 1,sentPI);
        smsManager.sendMultipartTextMessage(message.getTelephoneNumber(),null,textMessages, onSentIntents,null);
    }

    public void addOnSentListener(SMSSentListener listener){
        if(listener == null)
            throw new NullPointerException();
        onSentListeners.add(listener);
    }

    public void addOnReceiveListener(SMSRecieveListener listener){
        if(listener == null)
            throw new NullPointerException();
        onReceiveListeners.add(listener);
    }

    public static int getApplicationCode(){
        if(instance == null)
            throw new IllegalStateException("SMSController not initialized");
        return instance.applicationCode;
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
     * Method used by SMSReceiver to send a packet
     * @param packet
     */
    protected static void onReceive(SMSPacket packet, String telephoneNumber){
        //Use it only if it's for our application
        if(getInstance().applicationCode == packet.getApplicationCode()) {
            //Let's see if we already have the message stored
            boolean found = false;
            for (SMSReceivedMessage msg : getInstance().receivedMessages) {
                if (msg.getMessageCode() == packet.getMessageCode()) {
                    msg.addPacket(packet);
                    found = true;
                    break;
                }
            }
            //If not found then create a new Received Message
            if (!found) {
                getInstance().receivedMessages.add(new SMSReceivedMessage(packet, telephoneNumber));
            }
        }
    }

    /**
     * Call every listener once every packet of a message is arrived
     * @param message
     */
    protected static void callReceiveListeners(SMSReceivedMessage message){
        //Foreach listener call its method.
        for(SMSRecieveListener listener : getInstance().onReceiveListeners){
            listener.onSMSRecieve(message);
        }
        //Remove the message from the incomplete ones
        getInstance().receivedMessages.remove(message);
    }

    protected static SMSController getInstance(){
        if(instance == null)
            throw new IllegalStateException("SMSController not initialized");
        return instance;
    }

}
