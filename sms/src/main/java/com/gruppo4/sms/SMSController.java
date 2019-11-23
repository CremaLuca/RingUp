package com.gruppo4.sms;

import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * @author Alessandra Tonin
 * <p>
 * CODE REVIEW FOR VELLUDO AND TURCATO
 */

public class SMSController {

    final static int SMS_CHAR_NUMBER = 160;
    private static ArrayList<SMSReceivedListener> receivedListeners = new ArrayList<>();
    private static ArrayList<SMSSentListener> sentListeners = new ArrayList<>();


    /**
     * Constructor for SMSController object
     */
    public SMSController() {
    };

    /**
     * Send a SMS message
     *
     * @param message an object of SMSMessage class
     */
    public void sendMessage(SMSMessage message) {

        if (!(message.getTextMessage().length() > SMS_CHAR_NUMBER)) {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(message.getPhoneNumber(), null, message.getTextMessage(), null, null);
        }
    }

    /**
     * Add a listener to the list receivedListeners
     *
     * @param listener an object of SMSReceivedListener class
     */
    public static void addOnReceivedListeners(SMSReceivedListener listener) {
        receivedListeners.add(listener);
    }

    /**
     * Add a listener to the list sentListeners
     *
     * @param listener an object of SMSSentListener class
     */
    public static void addOnSentListeners(SMSSentListener listener) {
        sentListeners.add(listener);
    }

    /**
     * When a message arrives, call the receivedListener
     *
     * @param message a SMSMessage object
     */
    static void callReceivedListeners(SMSMessage message) {
        for (SMSReceivedListener listener : receivedListeners) {
            listener.onMessageReceived(message);
        }
    }

}
