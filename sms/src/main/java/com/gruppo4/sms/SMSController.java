package com.gruppo4.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

/**
 * @author Tommasini Marco
 */

public class SMSController {

    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";

    public static final String MSGBODY = "MSGBODY";
    public static final String DESTINATIONADDRESS = "DESTINATIONADDRESS";

    public static SMSSendListener listener;

    public enum SMSState {
        SENT_MESSAGE_SENT,
        SENT_ERROR_GENERIC_FAILURE,
        SENT_ERROR_NO_SERVICE,
        SENT_ERROR_NULL_PDU,
        SENT_ERROR_RADIO_OFF,
        DELIVERED_OK,
        DELIVERED_CANCELLED
    }

    /**
     * Sending msg and creating Intents for tracking Sent and Deliver events
     * @param context
     * @param msg
     */

    public void sendMessage(Context context, SMSMessage msg){

        Intent sentI = new Intent(SENT)
                .putExtra(MSGBODY, msg.getMsgBody())
                .putExtra(DESTINATIONADDRESS, msg.getDestinationAddress());
        Intent deliveredI = new Intent(DELIVERED)
                .putExtra(MSGBODY, msg.getMsgBody())
                .putExtra(DESTINATIONADDRESS, msg.getDestinationAddress());

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentI, 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, deliveredI, 0);

        SmsManager.getDefault().sendTextMessage(
                msg.getDestinationAddress(),
                null,
                msg.getMsgBody(),
                sentPI,
                deliveredPI);
    }
}
