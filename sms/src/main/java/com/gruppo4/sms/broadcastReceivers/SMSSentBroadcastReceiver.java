package com.gruppo4.sms.broadcastReceivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.gruppo4.sms.SMSMessage;
import com.gruppo4.sms.listeners.SMSSentListener;

public class SMSSentBroadcastReceiver extends BroadcastReceiver {

    private SMSMessage message;
    private SMSSentListener listener;

    public SMSSentBroadcastReceiver(SMSMessage message, SMSSentListener listener) {
        this.message = message;
        this.listener = listener;
    }

    public void setMessage(SMSMessage message) {
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
        SMSMessage.SentState state = SMSMessage.SentState.ERROR_GENERIC_FAILURE;
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                state = SMSMessage.SentState.MESSAGE_SENT;
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                state = SMSMessage.SentState.ERROR_GENERIC_FAILURE;
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                state = SMSMessage.SentState.ERROR_RADIO_OFF;
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                state = SMSMessage.SentState.ERROR_NULL_PDU;
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                state = SMSMessage.SentState.ERROR_NO_SERVICE;
                break;
            case SmsManager.RESULT_ERROR_LIMIT_EXCEEDED:
                state = SMSMessage.SentState.ERROR_LIMIT_EXCEEDED;
                break;
        }
        listener.onSMSSent(message, state);
    }
}
