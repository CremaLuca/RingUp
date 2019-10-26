package com.gruppo4.sms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.SparseArray;

import androidx.core.content.ContextCompat;

import com.gruppo4.sms.broadcastReceivers.SMSSentBroadcastReceiver;
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
    private SparseArray<ArrayList<SMSReceivedListener>> smsReceivedListeners;
    private int applicationCode;
    /**
     * List of incomplete messages received, when every packet of a message is arrived it gets removed from this list
     */
    private ArrayList<SMSReceivedMessage> receivedMessages;

    private Context context;


    private SMSController(int applicationCode) {
        smsReceivedListeners = new SparseArray<>();
        receivedMessages = new ArrayList<>();
        this.applicationCode = applicationCode;
    }

    /**
     * @param context is used to register the BroadcastReceiver
     * @param applicationCode
     * @return
     */
    public static SMSController setup(Context context, int applicationCode) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
            throw new SecurityException("Missing Manifest.permission.SEND_SMS permission, use requestPermissions() to be granted this permission runtime");
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED) {
            throw new SecurityException("Missing Manifest.permission.RECEIVE_SMS permission, use requestPermissions() to be granted this permission runtime");
        }
        if (instance != null) {
            //We can't have multiple application codes in the same app
            if (instance.applicationCode != applicationCode)
                throw new IllegalStateException("The SMSController is already initalized!");
        }
        else {
            instance = new SMSController(applicationCode);
        }
        instance.context = context;
        return instance;
    }

    /**
     * Send a SMSMessage, multiple packets could be sent
     *
     * @param message the message to be sent via SMS
     * @param listener called when the message is completely sent to the provider
     */
    public static void sendMessage(SMSMessage message, SMSSentListener listener) {
        //Create a PendingIntent, when the message will be sent from the android SMSManager a beacon of SMS_SENT will be intercepted by our OnSMSSent class
        PendingIntent sentPI = PendingIntent.getBroadcast(getInstance().context, 0, new Intent("SMS_SENT_" + message.getMessageCode()), 0);
        BroadcastReceiver receiver = new SMSSentBroadcastReceiver(message, listener);
        //Set the new BroadcastReceiver to intercept intents with the right filter
        getInstance().context.registerReceiver(receiver, new IntentFilter("SMS_SENT_" + message.getMessageCode()));
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

    /**
     * Subscribes the listener to be called once a message with the right code is received
     *
     * @param listener    a class that implements SMSReceivedListener
     * @param messageCode the identifier of the message
     */
    public static void addOnReceiveListener(SMSReceivedListener listener, int messageCode) {
        ArrayList<SMSReceivedListener> listeners = getInstance().smsReceivedListeners.get(messageCode);
        //If the array  hasn't been initialized, create it
        if (listeners == null)
            listeners = new ArrayList<>();
        //Add the listener to the list
        listeners.add(listener);
        //Put it back in the collection
        getInstance().smsReceivedListeners.put(messageCode, listeners);
    }

    /**
     * Returns the identifier of this application, used to avoid interfering with other application's messages
     * @return the application code
     */
    public static int getApplicationCode() {
        if (instance == null)
            throw new IllegalStateException("SMSController not initialized");
        return instance.applicationCode;
    }

    /**
     * Method used by OnSMSReceived to send a packet
     *
     * @param packet the sms content wrapped in a packet
     */
    public static void onReceive(SMSPacket packet, String telephoneNumber) {
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
     * Calls the specified listeners for the input message
     * @param message a fully reconstructed received message
     */
    static void callOnReceivedListeners(SMSReceivedMessage message) {
        ArrayList<SMSReceivedListener> receivedListeners = getInstance().smsReceivedListeners.get(message.getMessageCode());
        //If we have no listeners for that code, just ignore it
        if (receivedListeners == null)
            return;
        for (SMSReceivedListener listener : receivedListeners) {
            listener.onSMSReceived(message);
        }
    }

    private static SMSController getInstance() {
        if (instance == null)
            throw new IllegalStateException("SMSController is not setup!");
        return instance;
    }

}
