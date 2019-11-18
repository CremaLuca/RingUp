package com.gruppo4.sms;

/**
 * @author Tommasini Marco
 */

public interface SMSSendListener {
        void onSent(SMSMessage message, SMSController.SMSState state);
        void onDelivered(SMSMessage message, SMSController.SMSState state);
        void onReceived(SMSMessage message);
}
