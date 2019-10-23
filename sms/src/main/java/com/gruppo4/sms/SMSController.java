package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import com.gruppo4.sms.broadcastReceivers.SMSSentBroadcastReceiver;
import com.gruppo4.sms.interfaces.SMSReceivedListener;
import com.gruppo4.sms.interfaces.SMSSentListener;

import java.util.ArrayList;

public class SMSController {

    private static final String SENT_STRING = "SMS_SENT";
    private static ArrayList<SMSReceivedListener> receivedListeners = new ArrayList<>();

    public SMSController() {
    }

    //Send Message
    public static void sendMessage(SMSMessage message, Context context) {
        sendMessage(message, context, null);
    }

    public static void sendMessage(SMSMessage message, Context context, SMSSentListener listener) {
        SmsManager sms = SmsManager.getDefault();
        //Register the receiver for this message
        String sentString = SENT_STRING + "_" + message.getTelephoneNumber();
        BroadcastReceiver receiver = new SMSSentBroadcastReceiver(message, listener);
        context.registerReceiver(receiver, new IntentFilter(sentString));

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(sentString), 0);
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
}