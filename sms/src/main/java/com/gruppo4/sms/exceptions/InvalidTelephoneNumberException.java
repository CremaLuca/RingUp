package com.gruppo4.sms.exceptions;

import com.gruppo4.sms.SMSMessage;
import com.gruppo4.sms.utils.SMSChecks;

public class InvalidTelephoneNumberException extends Exception {

    private SMSChecks.TelephoneNumberState state;

    public InvalidTelephoneNumberException(String message, SMSChecks.TelephoneNumberState state) {
        super(message);
        this.state = state;
    }

    public InvalidTelephoneNumberException(Throwable cause,  SMSChecks.TelephoneNumberState state) {
        super(cause);
        this.state = state;
    }

    public SMSChecks.TelephoneNumberState getState(){
        return this.state;
    }
}
