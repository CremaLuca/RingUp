package com.gruppo4.sms.dataLink;

import android.app.PendingIntent;
import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * Wrapper for the Android Telephony Sms Library
 * This class is only used to interface with the core of Android
 */
class SMSCore {

    /**
     * Calls the library method to send a single message
     *
     * @param message     message to be sent
     * @param phoneNumber destination address
     * @param sentPI      pending intent for a broadcast
     */
    static void sendMessage(String message, String phoneNumber, PendingIntent sentPI) {
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, sentPI, null);
    }

    /**
     * Calls the library method to send multiple messages
     *
     * @param messages    arrayList of messages to be sent
     * @param phoneNumber destination address
     * @param sentPIs     arrayList of pending intents for a broadcast
     */
    static void sendMessages(ArrayList<String> messages, String phoneNumber, ArrayList<PendingIntent> sentPIs) {
        SmsManager.getDefault().sendMultipartTextMessage(phoneNumber, null, messages, sentPIs, null);
    }

}
