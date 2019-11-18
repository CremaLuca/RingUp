package com.gruppo4.sms;

/**
 * @author Alessandra Tonin
 *
 * CODE REVIEW FOR VELLUDO AND TURCATO
 */

/**
 * An interface that defines a SMSReceivedListener
 */
public interface SMSReceivedListener {
    void onMessageReceived(SMSMessage message);
}
