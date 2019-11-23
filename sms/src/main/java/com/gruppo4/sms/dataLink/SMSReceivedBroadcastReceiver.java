package com.gruppo4.sms.dataLink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.gruppo_4.preferences.PreferencesManager;

/**
 * BroadcastReceiver for
 */
public class SMSReceivedBroadcastReceiver extends BroadcastReceiver {

    public static final String INTENT_MESSAGE_NAME = "SMSMessage";
    public static final String SERVICE_CLASS_PREFERENCES_KEY = "ApplicationServiceClass";

    /**
     * Method called on message reception, parses the data and if the message is correctly formatted calls the receiver
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("SMSReceiver", "Received message from android broadcaster");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Object[] smsExtra = (Object[]) extras.get("pdus");
            String format = (String) extras.get("format");
            for (int i = 0; i < smsExtra.length; i++) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i], format);
                Log.v("SMSReceiver", "Parsing the message");
                reconstructMessage(context, sms.getOriginatingAddress(), sms.getMessageBody());
            }
        }
    }

    /**
     * Builds an SMSMessage from intent data
     *
     * @param context
     * @param address
     * @param messageBody
     */
    private void reconstructMessage(Context context, String address, String messageBody) {
        SMSMessage message = SMSMessageHandler.getInstance().parseMessage(messageBody, address);
        if (message != null && message.getApplicationID() == SMSHandler.getInstance(context).getApplicationCode()) {
            Log.v("SMSReceiver", "Message is for this application");
            callApplicationService(context, message);
        }
    }

    /**
     * Calls the current subscribed app service
     *
     * @param context
     * @param message
     */
    private void callApplicationService(Context context, SMSMessage message) {
        Class<?> listener = null;
        try {
            listener = Class.forName(PreferencesManager.getString(context, SERVICE_CLASS_PREFERENCES_KEY));
        } catch (ClassNotFoundException e) {
            Log.e("SMSReceiver", "Class searched could not be found");
        }
        if (listener != null) {
            Log.v("SMSReceiver", "Calling service");
            Intent serviceIntent = new Intent(context, listener);
            serviceIntent.putExtra(INTENT_MESSAGE_NAME, message);
            context.startService(serviceIntent);
        } else {
            Log.v("SMSReceiver", "The listener is null, nothing to perform then");
        }
    }
}
