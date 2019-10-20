package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SMSController {

    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";

    /**
     * Sending msg and creating Intents for tracking Sent and Deliver events
     * @param context
     * @param msg
     */

    void send(Context context, SMSMessage msg){

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

        SmsManager.getDefault().sendTextMessage(
                msg.getDestinationAddress(),
                null,
                msg.getMsgBody(),
                sentPI,
                deliveredPI);
    }
}
