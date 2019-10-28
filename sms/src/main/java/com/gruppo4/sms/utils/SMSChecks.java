package com.gruppo4.sms.utils;

import com.gruppo4.sms.SMSMessage;

public class SMSChecks {

    public enum TelephoneNumberState {
        TELEPHONE_NUMBER_VALID,
        TELEPHONE_NUMBER_TOO_SHORT,
        TELEPHONE_NUMBER_TOO_LONG,
        TELEPHONE_NUMBER_NO_COUNTRY_CODE,
        TELEPHONE_NUMBER_NOT_A_NUMBER
    }

    public enum MessageTextState {
        MESSAGE_TEXT_VALID,
        MESSAGE_TEXT_TOO_LONG
    }

    /**
     * Checks if the message is valid
     *
     * @param messageText the text to check
     * @return The state of the message after the tests
     */
    public static MessageTextState checkMessageText(String messageText) {
        if (messageText.length() > SMSMessage.MAX_MSG_TEXT_LEN) {
            return MessageTextState.MESSAGE_TEXT_TOO_LONG;
        }
        return MessageTextState.MESSAGE_TEXT_VALID;
    }

    public static final int MAX_TELEPHONE_NUMBER_LENGTH = 20;
    public static final int MIN_TELEPHONE_NUMBER_LENGTH = 7;

    /**
     * Checks if the phone number is valid
     *
     * @param telephoneNumber the phone number to check
     * @return The state of the telephone number after the tests
     */
    public static TelephoneNumberState checkTelephoneNumber(String telephoneNumber) {
        //Check if the number is shorter than the MAX.
        if (telephoneNumber.length() > MAX_TELEPHONE_NUMBER_LENGTH) {
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_LONG;
        }
        //Check if the number is longer than the MIN.
        if (telephoneNumber.length() < MIN_TELEPHONE_NUMBER_LENGTH) {
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_SHORT;
        }
        //Check if it's actually a number and doesn't contain anything else
        //First we have to remove the "+"
        if (!telephoneNumber.substring(1, telephoneNumber.length() - 1).matches("[0-9]+")) {
            return TelephoneNumberState.TELEPHONE_NUMBER_NOT_A_NUMBER;
        }
        //Check if there is a country code.
        if (telephoneNumber.charAt(0) != '+') {
            return TelephoneNumberState.TELEPHONE_NUMBER_NO_COUNTRY_CODE;
        }
        //If it passed all the tests we are sure the number is valid.
        return TelephoneNumberState.TELEPHONE_NUMBER_VALID;
    }
}
