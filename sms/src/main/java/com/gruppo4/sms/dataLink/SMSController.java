package com.gruppo4.sms.dataLink;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.gruppo4.sms.dataLink.broadcastReceivers.SMSSentBroadcastReceiver;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

import java.util.ArrayList;
import java.util.Random;

public class SMSController {

    private static SMSController instance; //singleton
    SMSSentBroadcastReceiver onSentReceiver;
    private Context context;
    private int appCode;
    private int nextID; //id chosen for the next message to send
    //list of listeners called when ALL packets of a message have been received
    private ArrayList<SMSReceivedListener> receivedListeners;
    private ArrayList<SMSMessage> incompleteMessages; //these are partially constructed messages

    /**
     * Constructor for the Controller, checks permissions and initializes lists
     *
     * @param context
     * @param applicationCode
     */
    private SMSController(Context context, int applicationCode) {
        this.context = context;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED)
            throw new SecurityException("Missing Manifest.permission.SEND_SMS permission, use requestPermissions() to be granted this permission runtime");

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED)
            throw new SecurityException("Missing Manifest.permission.RECEIVE_SMS permission, use requestPermissions() to be granted this permission runtime");

        receivedListeners = new ArrayList<>();
        this.appCode = applicationCode;
        nextID = (new Random()).nextInt(SMSMessage.MAX_ID + 1);
        Log.v("SMSController", "current ID: " + nextID);
        incompleteMessages = new ArrayList<>();
        onSentReceiver = new SMSSentBroadcastReceiver();
        context.registerReceiver(onSentReceiver, new IntentFilter("SMS_SENT"));
    }

    /**
     * Initializes the controller by setting the application code for received and sent messages
     *
     * @param context         The current app context
     * @param applicationCode A code for the app
     */
    public static void init(Context context, int applicationCode) {
        Log.v("SMSController", "SMSController initalized with application code " + applicationCode);
        instance = new SMSController(context, applicationCode);
    }

    public static SMSController getInstance() {
        if (instance == null)
            throw new IllegalStateException("The SMSController must be initialized!");
        else return instance;
    }

    /**
     * Send a SMSMessage, multiple packets could be sent
     *
     * @param message  the message to be sent via SMS
     * @param listener called when the message is completely sent to the provider
     */
    public static void sendMessage(SMSMessage message, SMSSentListener listener) {
        SMSController controller = getInstance();
        ArrayList<String> messages = message.getPacketsContent();
        ArrayList<PendingIntent> onSentIntents = setupPendingIntents(messages.size());

        controller.onSentReceiver.setListener(listener);
        controller.onSentReceiver.setMessage(message);
        for (String msg : messages) {
            Log.v("SMSController", "String to be sent, length: " + msg.length() + ", content: " + msg);
            ArrayList<String> dividedBySystem = SmsManager.getDefault().divideMessage(msg);
            if (dividedBySystem.size() > 1)
                throw new IllegalStateException("The message is too long (???)");
        }
        SmsManager.getDefault().sendMultipartTextMessage(message.getTelephoneNumber(), null, messages, onSentIntents, null);
    }

    /**
     * Subscribes the listener to be called once a message is completely received
     *
     * @param listener a class that implements SMSReceivedListener
     */
    public static void addOnReceiveListener(SMSReceivedListener listener) {
        Log.v("SMSController", "addOnReceiveListener called for " + listener.getClass());
        getInstance().receivedListeners.add(listener);
    }

    /**
     * Returns the identifier of this application, used to avoid interfering with other application's messages
     *
     * @return the application code
     */
    public static int getApplicationCode() {
        return getInstance().appCode;
    }

    /**
     * Method used by SMSReceivedBroadcastReceiver to store a packet
     *
     * @param packet the sms content wrapped in a packet
     */
    public static void onReceive(SMSPacket packet, String telephoneNumber) {
        Log.v("SMSController", "Packet received, id:" + packet.getMessageId() + " number:" + packet.getPacketNumber() + " total:" + packet.getTotalNumber() + " from:" + telephoneNumber + " content:" + packet.getMessageText());
        //Let's see if we already have the message stored
        boolean found = false;
        for (SMSMessage m : getInstance().incompleteMessages) {
            if (m.getTelephoneNumber().equals(telephoneNumber) && m.getMessageId() == packet.getMessageId()) {
                Log.v("SMSController", "Message for the id: " + packet.getMessageId() + " was already in the incomplete messages list");
                found = true;
                m.addPacket(packet);
                if (m.hasAllPackets()) {
                    Log.v("SMSController", "Message id:" + m.getMessageId() + " is now complete");
                    callOnReceivedListeners(m);
                    getInstance().incompleteMessages.remove(m);
                }
                break;
            }
        }
        //If not found then create a new Message
        if (!found) {
            Log.v("SMSController", "Creating a new incomplete messages for id:" + packet.getMessageId());
            SMSMessage m = new SMSMessage(telephoneNumber, packet);
            getInstance().incompleteMessages.add(m);
            if (m.hasAllPackets()) {
                Log.v("SMController", "The message id:" + m.getMessageId() + " is complete with one packet");
                callOnReceivedListeners(m);
                getInstance().incompleteMessages.remove(m);
            }
        }
    }

    /**
     * Calls every listener subscribed for the reception of a message
     *
     * @param message a complete message
     */
    private static void callOnReceivedListeners(SMSMessage message) {
        Log.v("SMController", "Calling all listener for message " + message.getMessageId());
        for (SMSReceivedListener listener : getInstance().receivedListeners) {
            listener.onSMSReceived(message);
        }

    }

    /**
     * Returns a sequential code for a new message to be sent
     *
     * @return a sequential code
     */
    static int getNewMessageId() {
        SMSController inst = getInstance();
        int current = inst.nextID;
        inst.nextID = (inst.nextID + 1) % (SMSMessage.MAX_PACKETS + 1);
        Log.v("SMSController", "Generating new packet id:" + inst.nextID);
        return current; //we send messages with id not greater than SMSMessage.MAX_PACKETS;
    }

    /**
     * Creates an empty array list of pending intents except for the last one
     *
     * @param numberOfPackets
     * @return
     */
    private static ArrayList<PendingIntent> setupPendingIntents(int numberOfPackets) {
        ArrayList<PendingIntent> onSentIntents = new ArrayList<>();
        for (int i = 0; i < numberOfPackets; i++) {
            onSentIntents.add(null); //we set all but the last listener to null
        }
        //we call the listener when all the packets have been sent, so last listener is not null
        PendingIntent sentPI = PendingIntent.getBroadcast(getInstance().context, 0, new Intent("SMS_SENT"), 0);
        onSentIntents.set(onSentIntents.size() - 1, sentPI);
        return onSentIntents;
    }

    public enum SentState {
        MESSAGE_SENT,
        ERROR_GENERIC_FAILURE,
        ERROR_RADIO_OFF,
        ERROR_NULL_PDU,
        ERROR_NO_SERVICE,
        ERROR_LIMIT_EXCEEDED
    }
}
