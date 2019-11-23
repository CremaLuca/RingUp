package com.gruppo4.sms.dataLink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceivedBroadcastReceiver extends BroadcastReceiver {

    public static final String INTENT_MESSAGE_NAME = "SMSMessage";

    public static Class listener;

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
            Log.v("SMSReceiver", "Extras length: " + smsExtra.length);
            for (int i = 0; i < smsExtra.length; i++) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i], format);
                Log.v("SMSReceiver", "Parsing the message");
                SMSMessage message = SMSMessageHandler.getInstance().parseMessage(sms.getMessageBody(), sms.getOriginatingAddress());
                if (message != null && message.getApplicationID() == SMSHandler.getInstance(context).getApplicationCode()) {

                    //Create intent and send a SMSMessage to the App Layer
                    Intent activityHelperI = new Intent("SMSApp");
                    activityHelperI.putExtra("Message", message);
                    context.sendBroadcast(activityHelperI);
                    Log.v("SMSReceiver", "Message is for this application");
                    if (listener != null) {
                        Log.v("SMSReceiver", "Calling service");
                        Intent serviceIntent = new Intent(context, listener);
                        serviceIntent.putExtra(INTENT_MESSAGE_NAME, message);
                        context.startService(serviceIntent);
                    }
                }
            }
        }
    }
}
