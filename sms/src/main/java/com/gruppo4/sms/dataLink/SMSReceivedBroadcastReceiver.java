package com.gruppo4.sms.dataLink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.gruppo_4.preferences.PreferencesManager;

/**
 * Broadcast receiver for received messages, called by Android.
 *
 * @author Luca Crema, Marco Mariotto
 * @since 29/11/2019
 *
 */
public class SMSReceivedBroadcastReceiver extends BroadcastReceiver {

    public static final String INTENT_MESSAGE_TAG = "SMSMessage";
    public static final String SERVICE_CLASS_PREFERENCES_KEY = "ApplicationServiceClass";

    /**
     * Parses message and calls listener
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }

        Object[] smsExtras = (Object[]) extras.get("pdus");
        String format = (String) extras.get("format");

        if(smsExtras == null) //could be null
            return;

        for (Object smsData : smsExtras) {
            SmsMessage receivedSMS = createMessageFromPdu(smsData, format);
            String smsContent = receivedSMS.getMessageBody();
            String phoneNumber = receivedSMS.getOriginatingAddress();

            if (phoneNumber == null) //could be null
                return;

            SMSMessage parsedMessage = SMSMessageHandler.getInstance().parseMessage(phoneNumber, smsContent);
            if (parsedMessage != null) {
                callApplicationService(context, parsedMessage);
            }
        }
    }

    /**
     * Calls the appropriate method to create a message from its pdus
     * @param smsData message pdus
     * @param format  available only on build version >= 23
     * @return the created message
     */
    private SmsMessage createMessageFromPdu(Object smsData, String format) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Requires android version >23
            return SmsMessage.createFromPdu((byte[]) smsData, format);
        }
        return SmsMessage.createFromPdu((byte[]) smsData);
    }

    /**
     * Calls the current subscribed app service
     *
     * @param context broadcast current context
     * @param message received message
     */
    private void callApplicationService(Context context, SMSMessage message) {
        Class<?> listener = null;
        try {
            listener = Class.forName(PreferencesManager.getString(context, SERVICE_CLASS_PREFERENCES_KEY));
        } catch (ClassNotFoundException e) {
            Log.e("SMSReceiver", "Service class to wake up could not be found");
        }
        if (listener == null)
            return;

        Intent serviceIntent = new Intent(context, listener);
        serviceIntent.putExtra(INTENT_MESSAGE_TAG, message);
        context.startService(serviceIntent);

    }
}
