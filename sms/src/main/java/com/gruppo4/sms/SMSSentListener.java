package com.gruppo4.sms;

/**
 * @author Alessandra Tonin
 */

/**
 * An interface that defines a SMSSentListener
 */
public interface SMSSentListener {
    void onMessageSent(SMSMessage message);
}
