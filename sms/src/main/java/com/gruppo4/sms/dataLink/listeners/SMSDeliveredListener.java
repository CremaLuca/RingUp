package com.gruppo4.sms.dataLink.listeners;

import com.gruppo4.sms.dataLink.SMSMessage;

/**
 * General interface for a listener for an SMS Delivered event
 *
 * @author Marco Cognolato
 */
public interface SMSDeliveredListener {

    /**
     * Callback for message sent to the provider or in case of error
     * @param message that's been delivered (or not)
     * @param deliveredState of the operation
     */
    void onSMSDelivered(SMSMessage message, SMSMessage.DeliveredState deliveredState);
}
