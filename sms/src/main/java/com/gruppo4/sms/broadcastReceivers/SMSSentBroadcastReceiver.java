package com.gruppo4.sms.broadcastReceivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;
import com.gruppo4.sms.listeners.SMSSentListener;

public class SMSSentBroadcastReceiver extends BroadcastReceiver {

    private SMSSentListener listener;
    SMSMessage message;

    public SMSSentBroadcastReceiver(SMSMessage  message, SMSSentListener listener) {
        this.listener = listener;
        this.message = message;
    }

    /**
     * This method is subscribed to the intent of a message sent, and will be called whenever a message is sent using this library.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SMSController.SentState state = SMSController.SentState.ERROR_GENERIC_FAILURE;
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                state = SMSController.SentState.MESSAGE_SENT;
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                state = SMSController.SentState.ERROR_GENERIC_FAILURE;
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                state = SMSController.SentState.ERROR_RADIO_OFF;
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                state = SMSController.SentState.ERROR_NULL_PDU;
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                state = SMSController.SentState.ERROR_NO_SERVICE;
                break;
            case SmsManager.RESULT_ERROR_LIMIT_EXCEEDED:
                state = SMSController.SentState.ERROR_LIMIT_EXCEEDED;
                break;
        }
        listener.onSMSSent(message, state);
    }
}
