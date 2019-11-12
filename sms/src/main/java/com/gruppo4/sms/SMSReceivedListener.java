package com.gruppo4.sms;

/**
 * @author Alessandra Tonin
 */

/**
 * An interface that defines a SMSReceivedListener
 */
public interface SMSReceivedListener {
    void onMessageReceived(SMSMessage message);
}
