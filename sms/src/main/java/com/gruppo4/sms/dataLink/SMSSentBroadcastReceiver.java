package com.gruppo4.sms.dataLink;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

public class SMSSentBroadcastReceiver extends BroadcastReceiver {

    private SMSSentListener listener;
    private SMSMessage message;
    private SMSHandler.SentState sentState = SMSHandler.SentState.MESSAGE_SENT;
    private int packetsCounter;

    /**
     * Constructor for the BroadcastReceiver.
     *
     * @param message  message that will be sent.
     * @param listener listener to be called when the operation is completed successfully or not.
     */
    public SMSSentBroadcastReceiver(@NonNull SMSMessage message, SMSSentListener listener) {
        this.listener = listener;
        this.message = message;
        packetsCounter = 0;
    }

    /**
     * @param listener a listener to be called once the message is sent.
     */
    public void setListener(SMSSentListener listener) {
        Log.v("SMSSentReceiver", "Changed listener to class:" + listener.getClass());
        this.listener = listener;
    }

    /**
     * @param message a message to pass to the listener once it is sent.
     */
    public void setMessage(SMSMessage message) {
        Log.v("SMSSentReceiver", "Changed message to id:" + message.getMessageId());
        this.message = message;
        packetsCounter = 0;
    }

    /**
     * This method is subscribed to the intent of a message sent, and will be called whenever a message is sent using this library.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SMSHandler.SentState state;
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                state = SMSHandler.SentState.MESSAGE_SENT;
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                state = SMSHandler.SentState.ERROR_RADIO_OFF;
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                state = SMSHandler.SentState.ERROR_NULL_PDU;
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                state = SMSHandler.SentState.ERROR_NO_SERVICE;
                break;
            case SmsManager.RESULT_ERROR_LIMIT_EXCEEDED:
                state = SMSHandler.SentState.ERROR_LIMIT_EXCEEDED;
                break;
            default:
                state = SMSHandler.SentState.ERROR_GENERIC_FAILURE;
                Log.d("SMSSentReceiver", "Generic error for message id: " + message.getMessageId());
                break;
        }
        Log.v("SMSSentReceiver", "Sent a packet with state: " + state);

        setSentState(state);
        packetsCounter++;

        if (checkCounter(packetsCounter)) {
            if (listener != null)
                listener.onSMSSent(message, sentState);
        }
    }

    /**
     * Updates the sent state to the current one
     *
     * @param sentState state for the current packet
     */
    private void setSentState(SMSHandler.SentState sentState) {
        //The state is modified ONLY IF THE CURRENT STATE IS OK. If a single packet has given an error the state is error
        if (this.sentState == SMSHandler.SentState.MESSAGE_SENT)
            this.sentState = sentState;
    }

    /**
     * Checks if the number of packets we sent is the same as the total number of packets
     *
     * @param packetsCounter number of packets sent as of now
     * @return if we have sent all the packets we had to send and if we can call the listener
     */
    private boolean checkCounter(int packetsCounter) {
        return packetsCounter >= message.getPackets().length;
    }
}
