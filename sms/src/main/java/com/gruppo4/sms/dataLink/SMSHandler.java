package com.gruppo4.sms.dataLink;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;

import com.gruppo4.sms.dataLink.listeners.SMSSentListener;
import com.gruppo_4.preferences.PreferencesManager;

import java.util.ArrayList;

class SMSHandler {


    private static final String MESSAGE_SEQUENTIAL_CODE_PREFERENCES_KEY = "MessageSequentialCode";
    private static final String APPLICATION_CODE_PREFERENCES_KEY = "ApplicationCode";
    private static final String SENT_MESSAGE_INTENT_ACTION_PREFIX = "SENT_SMS";
    private static ArrayList<SMSMessage> incompleteMessages = new ArrayList<>(); //these are partially constructed messages


    /**
     * Setup the handler, checks permissions and sets the application code
     *
     * @param applicationCode an identifier for the current application
     */
    static void setup(Context context, int applicationCode) {
        if (!checkApplicationCodeIsValid(applicationCode))
            throw new IllegalStateException("Application code not valid, check it with checkApplicationCodeIsValid() first");

        setApplicationCode(context, applicationCode);
    }

    /**
     * Send a SMSMessage, multiple packets could be sent
     * Requires Manifest.permission.SEND_SMS permission
     *
     * @param message  the message to be sent via SMS
     * @param listener called when the message is completely sent to the provider
     */
    static void sendMessage(Context context, final SMSMessage message, SMSSentListener listener) {
        ArrayList<String> messages = message.getPacketsContent();

        String intentAction = SENT_MESSAGE_INTENT_ACTION_PREFIX + "_" + message.getMessageId();
        ArrayList<PendingIntent> onSentIntents = setupPendingIntents(context, messages.size(), intentAction);

        //Setup broadcast receiver
        SMSSentBroadcastReceiver onSentReceiver = new SMSSentBroadcastReceiver(message, listener);
        context.registerReceiver(onSentReceiver, new IntentFilter(intentAction));

        //Check on packets
        for (String msg : messages) {
            Log.v("SMSHandler", "String to be sent, length: " + msg.length() + ", content: " + msg);
            ArrayList<String> dividedBySystem = SmsManager.getDefault().divideMessage(msg);
            if (dividedBySystem.size() > 1)
                throw new IllegalStateException("The message is too long (???)");
        }
        SMSCore.sendMessages(messages, message.getPeer().getAddress(), onSentIntents);
    }


    /**
     * Method used by SMSReceivedBroadcastReceiver to store a packet
     *
     * @param packet the sms content wrapped in a packet
     */
    static void onReceive(Context ctx, SMSPacket packet, String telephoneNumber) {
        Log.v("SMSHandler", "Packet received, id:" + packet.getMessageId() + " number:" + packet.getPacketNumber() + " total:" + packet.getTotalNumber() + " from:" + telephoneNumber + " content:" + packet.getMessageText());
        //Let's see if we already have the message stored
        boolean found = false;
        for (SMSMessage message : incompleteMessages) {
            if (message.getPeer().getAddress().equals(telephoneNumber) && message.getMessageId() == packet.getMessageId()) {
                Log.v("SMSHandler", "Message for the id: " + packet.getMessageId() + " was already in the incomplete messages list");
                found = true;
                message.addPacket(packet);
                if (message.hasAllPackets()) {
                    Log.v("SMSHandler", "Message id:" + message.getMessageId() + " is now complete");
                    SMSManager.getInstance(ctx).callReceivedMessageListener(message);
                    incompleteMessages.remove(message);
                }
                break;
            }
        }
        //If not found then create a new Message
        if (!found) {
            Log.v("SMSHandler", "Creating a new incomplete messages for id:" + packet.getMessageId());
            SMSMessage message = new SMSMessage(new SMSPeer(telephoneNumber), packet);
            incompleteMessages.add(message);
            if (message.hasAllPackets()) {
                Log.v("SMController", "The message id:" + message.getMessageId() + " is complete with one packet");
                SMSManager.getInstance(ctx).callReceivedMessageListener(message);
                incompleteMessages.remove(message);
            }
        }
    }

    /**
     * Returns a sequential code for a new message to be sent
     *
     * @return a sequential code
     */
    static int getNewMessageId(Context ctx) {
        return PreferencesManager.shiftInt(ctx, MESSAGE_SEQUENTIAL_CODE_PREFERENCES_KEY, SMSMessage.MAX_ID);
    }

    /**
     * Creates an empty array list of pending intents except for the last one
     *
     * @param numberOfPackets
     * @return
     */
    private static ArrayList<PendingIntent> setupPendingIntents(Context context, int numberOfPackets, String intentAction) {
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(intentAction), 0);
        ArrayList<PendingIntent> onSentIntents = new ArrayList<>();
        for (int i = 0; i < numberOfPackets; i++) {
            onSentIntents.add(sentPI);
        }
        return onSentIntents;
    }

    /**
     * @param applicationCode a integer code.
     * @return true if the parameter application code is valid.
     */
    static boolean checkApplicationCodeIsValid(int applicationCode) {
        return applicationCode > 0 && applicationCode < 1000;
    }

    /**
     * Writes in the memory the application code.
     *
     * @param ctx             current app or service context.
     * @param applicationCode a valid application code, checked with checkApplicationCodeIsValid.
     */
    private static void setApplicationCode(Context ctx, int applicationCode) {
        PreferencesManager.setInt(ctx, APPLICATION_CODE_PREFERENCES_KEY, applicationCode);
    }

    /**
     * Gets the identifier application code from the memory.
     *
     * @param ctx current app or service context.
     * @return the current application code
     */
    static int getApplicationCode(Context ctx) {
        int appCode = PreferencesManager.getInt(ctx, APPLICATION_CODE_PREFERENCES_KEY);
        if (appCode < 0)
            throw new IllegalStateException("Unable to perform the request, the SMS library has never been setup, call the setup() method at least once");
        return appCode;
    }

    /**
     * Checks if SEND_SMS permission is granted
     *
     * @param ctx a valid context
     * @return true if permission is granted
     */
    static boolean checkSendPermission(Context ctx) {
        boolean result = ctx.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        Log.d("1","return del metodo checkSendPermission: "+ result);
        return result;
    }

    /**
     * Checks if RECEIVE_SMS permission is granted
     *
     * @param ctx a valid context
     * @return true if permission is granted
     */
    static boolean checkReceivePermission(Context ctx) {
        boolean result = ctx.checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        Log.d("1","return del metodo checkSendPermission: "+ result);
        return result;
    }

    /**
     * Checks if both SEND_SMS & RECEIVE_SMS permissions are granted
     *
     * @param ctx a valid context
     * @return true if both permissions are granted
     */
    static boolean checkPermissions(Context ctx) {
        return checkSendPermission(ctx) && checkReceivePermission(ctx);
    }

}
