package com.gruppo4.sms.dataLink.listeners;


import com.gruppo4.sms.dataLink.SMSMessage;

/**
 * Listener for sent messages
 *
 * @author Alessandra Tonin
 */
public interface SMSSentListener {

    /**
     * Callback for message sent to the provider or in case of error
     * @param message that's been sent/not sent
     * @param sentState of the operation
     */
    void onSMSSent(SMSMessage message, SMSMessage.SentState sentState);

}
