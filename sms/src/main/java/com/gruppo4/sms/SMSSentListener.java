package com.gruppo4.sms;

/*
 * an interface that defines a SMSSentListener
 */
public interface SMSSentListener {
    void onMessageSent(SMSMessage message);
}
