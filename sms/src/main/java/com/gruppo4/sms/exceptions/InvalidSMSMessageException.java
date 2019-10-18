package com.gruppo4.sms.exceptions;

public class InvalidSMSMessageException extends Exception {

    public InvalidSMSMessageException(String message) {
        super(message);
    }

    public InvalidSMSMessageException(Throwable cause) {
        super(cause);
    }
}
