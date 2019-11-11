package com.gruppo4.sms.dataLink;

/**
 * @author Alberto Ursino
 */
public class Variables {

    /**
     * Application code
     */
    protected static final int VALID_APPLICATION_CODE = 1;
    protected static final int TOO_BIG_APPLICATION_CODE = 1000;
    protected static final int TOO_SMALL_APPLICATION_CODE = -1;
    /**
     * Message
     */
    protected static final String HIDDEN_CHARACTER = (char) 0x02 + "";
    protected static final String SEPARATOR_CHARACTER = "_";
    protected static final int MAX_MSG_TEXT_LEN = SMSMessage.MAX_MSG_TEXT_LEN;
    protected static final String MAIN_MESSAGE = "Test message";
    protected static final String VALID_MESSAGE_START = HIDDEN_CHARACTER + VALID_APPLICATION_CODE + SEPARATOR_CHARACTER;
    protected static final String VALID_TEXT_MESSAGE = VALID_MESSAGE_START + MAIN_MESSAGE;
    /**
     * Telephone Numbers
     */
    protected static final String WRONG_TELEPHONE_NUMBER = "2lb";
    protected static final String VALID_TELEPHONE_NUMBER = "+391111111111";
    /**
     * Warnings
     */
    protected static final String SHOULD_NOT = "Should not have thrown an exception";
    protected static final String SHOULD_THROW = "Should have thrown an exception";
}
