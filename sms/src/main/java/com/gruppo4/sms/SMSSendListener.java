package com.gruppo4.sms;

public interface SMSSendListener {
        void onDelivered(SMSMessage message, SMSController.SMSState state);
        void onSent(SMSMessage message, SMSController.SMSState state);
}
