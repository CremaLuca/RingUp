package com.gruppo4.sms.exceptions;

public class InvalidTelephoneNumberException extends Exception {

    public InvalidTelephoneNumberException(String message) {
        super(message);
    }

    public InvalidTelephoneNumberException(Throwable cause) {
        super(cause);
    }
}
