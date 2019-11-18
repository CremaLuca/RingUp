package com.gruppo4.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

/**
 * @author Tommasini Marco
 *
 * Code Review for Raimondi and Martignago
 */

public class SMSSent extends BroadcastReceiver {

    /**
     * Called when Sent event occurs, trigger SMSSendListener updating the state
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SMSController.SMSState state = SMSController.SMSState.SENT_ERROR_GENERIC_FAILURE;
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                state = SMSController.SMSState.SENT_MESSAGE_SENT;
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                state = SMSController.SMSState.SENT_ERROR_GENERIC_FAILURE;
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                state = SMSController.SMSState.SENT_ERROR_NO_SERVICE;
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                state = SMSController.SMSState.SENT_ERROR_NULL_PDU;
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                state = SMSController.SMSState.SENT_ERROR_RADIO_OFF;
                break;
        }
        SMSMessage msg = new SMSMessage(
                intent.getStringExtra(SMSController.MSGBODY),
                intent.getStringExtra(SMSController.DESTINATIONADDRESS));

        SMSController.listener.onSent(msg, state);
    }
}
