package com.gruppo4.sms;

/**
 * @author Alessandra Tonin
 * <p>
 * CODE REVIEW FOR VELLUDO AND TURCATO
 */

public class SMSMessage {

    private String number;
    private String text;
    final static int SMS_CHAR_NUMBER = 160;


    /**
     * Constructor for SMSMessage object
     *
     * @param phoneNum the phone number to which we send the message
     * @param smsTxt   the text of the message
     */
    public SMSMessage(String phoneNum, String smsTxt) {
        if (!(smsTxt.length() > SMS_CHAR_NUMBER)) {
            number = phoneNum;
            text = smsTxt;
        }
    }

    /**
     * Gets the phone number to which we send the message
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return number;
    }

    /**
     * Gets the message text
     *
     * @return the message text
     */
    public String getTextMessage() {
        return text;
    }
}
