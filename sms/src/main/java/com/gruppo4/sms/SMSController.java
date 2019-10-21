package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;

import com.gruppo4.sms.listeners.SMSReceivedListener;
import com.gruppo4.sms.listeners.SMSSentListener;

import java.util.ArrayList;

public class SMSController {

    /**
     * SINGLETON
     */
    private static SMSController instance;
    /**
     * List of receive listeners that are triggered on message received
     */
    private ArrayList<SMSReceivedListener> onReceiveListeners;
    private int applicationCode;
    /**
     * List of incomplete messages received, when every packet of a message is arrived it gets removed from this list
     */
    private ArrayList<SMSReceivedMessage> receivedMessages;

    private SMSController(int applicationCode) {
        onReceiveListeners = new ArrayList<>();
        receivedMessages = new ArrayList<>();
        this.applicationCode = applicationCode;
    }

    public static SMSController setup(int applicationCode) {
        if (instance != null) {
            //We can't have multiple application codes in the same app
            if (instance.applicationCode != applicationCode) {
                throw new IllegalStateException("The SMSController is already initalized!");
            } else {
                return instance;
            }
        }
        instance = new SMSController(applicationCode);
        return instance;
    }

    /**
     * Send a SMSMessage, multiple packets could be sent
     *
     * @param context
     * @param message
     */
    public static void sendMessage(Context context, SMSMessage message, SMSSentListener listener) {
        //Create a PendingIntent, when the message will be sent from the android SMSManager a beacon of SMS_SENT will be intercepted by our OnSMSSent class
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT_" + message.getMessageCode()), 0);
        BroadcastReceiver receiver = new OnSMSSent(message, listener);
        //Set the new BroadcastReceiver to intercept intents with the right filter
        context.registerReceiver(receiver, new IntentFilter("SMS_SENT_" + message.getMessageCode()));
        //Retrieve the Android default smsManager
        SmsManager smsManager = SmsManager.getDefault();
        //Split the message in packets (multiple SMSs)
        SMSPacket[] packets = message.getPackets();

        ArrayList<String> textMessages = new ArrayList<>();
        ArrayList<PendingIntent> onSentIntents = new ArrayList<>();

        for (SMSPacket packet : packets) {
            textMessages.add(packet.getSMSOutput());
            onSentIntents.add(null); //Empty, will explain later why
            Log.d("SMSController", "Packet_" + packet.getPacketNumber() + ":" + packet.getSMSOutput());
        }
        //Except for the last pending intent that will be a real callback, we want it ONLY when the last packet is sent
        onSentIntents.set(onSentIntents.size() - 1, sentPI);
        smsManager.sendMultipartTextMessage(message.getTelephoneNumber(), null, textMessages, onSentIntents, null);
    }

    public static void addOnReceiveListener(SMSReceivedListener listener) {
        if (listener == null)
            throw new NullPointerException();
        getInstance().onReceiveListeners.add(listener);
    }

    public static int getApplicationCode() {
        if (instance == null)
            throw new IllegalStateException("SMSController not initialized");
        return instance.applicationCode;
    }

    /**
     * Method used by OnSMSReceived to send a packet
     *
     * @param packet
     */
    static void onReceive(SMSPacket packet, String telephoneNumber) {
        //Use it only if it's for our application
        if (getInstance().applicationCode == packet.getApplicationCode()) {
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
     *
     * @param message
     */
    static void callReceiveListeners(SMSReceivedMessage message) {
        //Foreach listener call its method.
        for (SMSReceivedListener listener : getInstance().onReceiveListeners) {
            listener.onSMSReceived(message);
        }
        //Remove the message from the incomplete ones
        getInstance().receivedMessages.remove(message);
    }

    static SMSController getInstance() {
        if (instance == null)
            throw new IllegalStateException("SMSController is not setup!");
        return instance;
    }

}
