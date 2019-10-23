package com.gruppo4.sms;

/*
 * an interface that defines a SMSReceivedListener
 */
public interface SMSReceivedListener {
    void onMessageReceived(SMSMessage message);
}
