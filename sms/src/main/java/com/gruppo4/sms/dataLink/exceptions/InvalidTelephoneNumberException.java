package com.gruppo4.sms.dataLink.exceptions;

import com.gruppo4.sms.dataLink.SMSMessage;

public class InvalidTelephoneNumberException extends Exception {

    private SMSMessage.TelephoneNumberState state;

    public InvalidTelephoneNumberException(String message, SMSMessage.TelephoneNumberState state) {
        super(message);
        this.state = state;
    }

    public InvalidTelephoneNumberException(Throwable cause, SMSMessage.TelephoneNumberState state) {
        super(cause);
        this.state = state;
    }

    public SMSMessage.TelephoneNumberState getState() {
        return this.state;
    }
}
