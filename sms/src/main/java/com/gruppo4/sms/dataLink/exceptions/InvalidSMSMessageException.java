package com.gruppo4.sms.dataLink.exceptions;

import com.gruppo4.sms.dataLink.SMSMessage;

public class InvalidSMSMessageException extends Exception {

    private SMSMessage.MessageTextState state;

    public InvalidSMSMessageException(String message, SMSMessage.MessageTextState state) {
        super(message);
        this.state = state;
    }

    public InvalidSMSMessageException(Throwable cause, SMSMessage.MessageTextState state) {
        super(cause);
        this.state = state;
    }

    public SMSMessage.MessageTextState getState() {
        return this.state;
    }
}
