package com.gruppo4.sms.exceptions;

import com.gruppo4.sms.utils.SMSChecks;

public class InvalidSMSMessageException extends Exception {

    private SMSChecks.MessageTextState state;

    public InvalidSMSMessageException(String message, SMSChecks.MessageTextState state) {
        super(message);
        this.state = state;
    }

    public InvalidSMSMessageException(Throwable cause, SMSChecks.MessageTextState state) {
        super(cause);
        this.state = state;
    }

    public SMSChecks.MessageTextState getState() {
        return this.state;
    }
}
