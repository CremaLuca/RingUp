package com.gruppo4.sms;

public class SMSMessage {

    public static final int MAX_TELEPHONE_NUMBER_LENGTH = 20;
    public static final int MIN_TELEPHONE_NUMBER_LENGTH = 7;
    public static final int MAX_MESSAGE_LENGTH = 159;
    private String textMessage;
    private String telephoneNumber;
    private SentState state;

    public SMSMessage(String telephoneNumber, String text) {
        this.textMessage = text;
        this.telephoneNumber = telephoneNumber;
    }

    public static boolean checkTelephoneNumber(String telephoneNumber) {
        //Check if the number is shorter than the MAX.
        if (telephoneNumber.length() > MAX_TELEPHONE_NUMBER_LENGTH) {
            return false;
        }
        //Check if the number is longer than the MIN.
        if (telephoneNumber.length() < MIN_TELEPHONE_NUMBER_LENGTH) {
            return false;
        }
        //Check if it's actually a number and doesn't contain anything else
        //First we have to remove the "+"
        if (!telephoneNumber.substring(1, telephoneNumber.length() - 1).matches("[0-9]+")) {
            return false;
        }
        //Check if there is a country code.
        return telephoneNumber.substring(0, 1).equals("+");
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getMessage() {
        return textMessage;
    }

    public SentState getState() {
        return state;
    }

    public void setSentState(SentState state) {
        this.state = state;
    }

    public enum SentState {
        NOT_SENT,
        MESSAGE_SENT,
        ERROR_GENERIC_FAILURE,
        ERROR_RADIO_OFF,
        ERROR_NULL_PDU,
        ERROR_NO_SERVICE,
        ERROR_LIMIT_EXCEEDED
    }

}
