package com.gruppo4.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Tommasini Marco
 * Code Review for Raimondi and Martignago
 */

public class SMSDelivered extends BroadcastReceiver {

    /**
     * Called when Delivered event occurs, trigger SMSSendListener updating the state
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SMSController.SMSState state = SMSController.SMSState.DELIVERED_CANCELLED;
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                state = SMSController.SMSState.DELIVERED_OK;
                break;
            case Activity.RESULT_CANCELED:
                state = SMSController.SMSState.DELIVERED_CANCELLED;
                break;
        }
        SMSMessage msg = new SMSMessage(
                intent.getStringExtra(SMSController.MSGBODY),
                intent.getStringExtra(SMSController.DESTINATIONADDRESS));

        SMSController.listener.onDelivered(msg, state);
    }
}
