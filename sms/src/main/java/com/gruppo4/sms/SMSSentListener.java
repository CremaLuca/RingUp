package com.gruppo4.sms;

/**
 * @author Alessandra Tonin
 *
 * CODE REVIEW FOR VELLUDO AND TURCATO
 */

/**
 * An interface that defines a SMSSentListener
 */
public interface SMSSentListener {
    void onMessageSent(SMSMessage message);
}
