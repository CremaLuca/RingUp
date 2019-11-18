package com.gruppo4.sms;

/**
 * @author Tommasini Marco
 * Code Review for Raimondi and Martignago
 */

public interface SMSSendListener {
        void onSent(SMSMessage message, SMSController.SMSState state);
        void onDelivered(SMSMessage message, SMSController.SMSState state);
        void onReceived(SMSMessage message);
}
