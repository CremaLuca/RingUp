package com.gruppo4.sms.dataLink;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

/**
 * Broadcast receiver for sent messages, called by Android Library.
 * Must be instantiated and set as receiver with context.registerReceiver(...)
 */
class SMSSentBroadcastReceiver extends BroadcastReceiver {

    private SMSSentListener listener;
    private SMSMessage message;
    private SMSMessage.SentState sentState = SMSMessage.SentState.MESSAGE_SENT;
    private int packetsCounter;

    /**
     * Constructor for the BroadcastReceiver.
     *
     * @param message  message that will be sent.
     * @param listener listener to be called when the operation is completed successfully or not.
     */
    SMSSentBroadcastReceiver(@NonNull final SMSMessage message, SMSSentListener listener) {
        this.listener = listener;
        this.message = message;
        packetsCounter = 0;
    }

    /**
     * @param listener a listener to be called once the message is sent.
     */
    void setListener(SMSSentListener listener) {
        Log.v("SMSSentReceiver", "Changed listener to class:" + listener.getClass());
        this.listener = listener;
    }

    /**
     * @param message a message to pass to the listener once it is sent.
     */
    void setMessage(@NonNull final SMSMessage message) {
        Log.v("SMSSentReceiver", "Changed message to id:" + message.getMessageID());
        this.message = message;
        packetsCounter = 0;
    }

    /**
     * This method is subscribed to the intent of a message sent, and will be called whenever a message is sent using this library.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SMSMessage.SentState state;
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                state = SMSMessage.SentState.MESSAGE_SENT;
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
            default:
                state = SMSMessage.SentState.ERROR_GENERIC_FAILURE;
                Log.d("SMSSentReceiver", "Generic error for message id: " + message.getMessageID());
                break;
        }
        Log.v("SMSSentReceiver", "Sent a packet with state: " + state);

        setSentState(state);
        packetsCounter++; //Updates the number of packets sent

        if (checkCounter()) { //Call the listener if the message is completely sent
            if (listener != null)
                listener.onSMSSent(message, sentState);
            context.unregisterReceiver(this);
        }
    }

    /**
     * Updates the message sent state, the state is NOT updated if the current state is an error
     *
     * @param sentState state for the current packet
     */
    private void setSentState(SMSMessage.SentState sentState) {
        //The state is modified ONLY IF THE CURRENT STATE IS OK. If a single packet has given an error the state is error
        if (this.sentState == SMSMessage.SentState.MESSAGE_SENT)
            this.sentState = sentState;
    }

    /**
     * Checks if the number of packets we sent is the same as the total number of packets
     *
     * @return if we have sent all the packets we had to send and if we can call the listener
     */
    private boolean checkCounter() {
        if (packetsCounter > message.getPackets().length) {
            //Not an error to die on
            Log.w("SMSSentBroadcast", "WARNING: the sent broadcast receiver sent more packets than it should");
        }
        return packetsCounter >= message.getPackets().length;
    }
}
