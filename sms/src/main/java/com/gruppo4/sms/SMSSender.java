package com.gruppo4.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SMSSender extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SMSController.SMSSentState state = SMSController.SMSSentState.ERROR_GENERIC_FAILURE;
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                state = SMSController.SMSSentState.MESSAGE_SENT;
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                state = SMSController.SMSSentState.ERROR_GENERIC_FAILURE;
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                state = SMSController.SMSSentState.ERROR_RADIO_OFF;
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                state = SMSController.SMSSentState.ERROR_NULL_PDU;
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                state = SMSController.SMSSentState.ERROR_NO_SERVICE;
                break;
            case SmsManager.RESULT_ERROR_LIMIT_EXCEEDED:
                state = SMSController.SMSSentState.ERROR_LIMIT_EXCEEDED;
                break;
        }
        SMSController.onSent(state);
    }
}
