package com.gruppo4.sms;

public interface SMSSentListener {

    void onSentReceived(SMSMessage message, SMSController.SentStatus status);

}
