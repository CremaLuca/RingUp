package com.gruppo4.sms.dataLink;

import com.gruppo4.communication.Peer;

public class SMSPeer extends Peer<String> {

    public static final int MAX_TELEPHONE_NUMBER_LENGTH = 20;
    public static final int MIN_TELEPHONE_NUMBER_LENGTH = 7;

    /**
     * @param telephoneNumber a valid telephone number (checkTelephoneNumber state must be TELEPHONE_NUMBER_VALID)
     */
    public SMSPeer(String telephoneNumber) {
        super(telephoneNumber);
    }

    /**
     * Checks if the phone number is valid
     *
     * @param telephoneNumber the phone number to check
     * @return The state of the telephone number after the tests
     */
    public static TelephoneNumberState checkPhoneNumber(String telephoneNumber) {
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

    /**
     * Checks if the phone number is valid
     *
     * @return The state of the telephone number after the tests
     */
    public TelephoneNumberState checkPhoneNumber() {
        return checkPhoneNumber(this.getAddress());
    }

    public enum TelephoneNumberState {
        TELEPHONE_NUMBER_VALID,
        TELEPHONE_NUMBER_TOO_SHORT,
        TELEPHONE_NUMBER_TOO_LONG,
        TELEPHONE_NUMBER_NO_COUNTRY_CODE,
        TELEPHONE_NUMBER_NOT_A_NUMBER
    }

}
