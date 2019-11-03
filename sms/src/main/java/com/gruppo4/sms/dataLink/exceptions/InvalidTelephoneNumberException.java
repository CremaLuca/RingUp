package com.gruppo4.sms.dataLink.exceptions;

import com.gruppo4.sms.dataLink.SMSPeer;

public class InvalidTelephoneNumberException extends Exception {

    private SMSPeer.TelephoneNumberState state;

    public InvalidTelephoneNumberException(String message, SMSPeer.TelephoneNumberState state) {
        super(message);
        this.state = state;
    }

    public InvalidTelephoneNumberException(Throwable cause, SMSPeer.TelephoneNumberState state) {
        super(cause);
        this.state = state;
    }

    public SMSPeer.TelephoneNumberState getState() {
        return this.state;
    }
}
