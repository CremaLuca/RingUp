package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.gruppo4.sms.interfaces.SMSReceivedListener;

import java.util.ArrayList;

public class SMSController {

    private static final String SENT_STRING = "SMS_SENT";
    public static ArrayList<SMSReceivedListener> receivedListeners = new ArrayList<>();

    public SMSController() {
    }

    //Send Message
    public static void sendMessage(SMSMessage message, Context context) {

        SmsManager sms = SmsManager.getDefault();

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT_STRING), 0);
        sms.sendTextMessage(message.getTelephoneNumber(), null, message.getMessage(), sentPI, null);
    }

    public static void addOnReceivedListener(SMSReceivedListener smsReceivedListener) {
        receivedListeners.add(smsReceivedListener);
    }

    public static void callReceivedListener(SMSMessage message) {
        for (SMSReceivedListener listener : receivedListeners) {
            listener.onMessageReceived(message);
        }
    }

    public enum SentStatus {
        SENT,
        ERROR
    }
}