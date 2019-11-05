package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Test;

public class SMSMessageUnitTest {

    SMSMessage smsMessage;
    private static final String NORMAL_TEXT_MESSAGE = "Test message";
    private static final int MAX_LENGTH_TEXT_MESSAGE = 139860; //smsMessage.MAX_MSG_TEXT_LEN
    private static final String TOO_LONG_TEXT_MESSAGE = new String(new char[MAX_LENGTH_TEXT_MESSAGE * 2]).replace('\0', ' ');
    private static final String MAX_SIZE_TEXT_MESSAGE_P1 = new String(new char[MAX_LENGTH_TEXT_MESSAGE + 1]).replace('\0', ' ');
    private static final String MAX_SIZE_TEXT_MESSAGE_M1 = new String(new char[MAX_LENGTH_TEXT_MESSAGE - 1]).replace('\0', ' ');

    @Test
    public void checkMessageText_smsText_isTooLong() {
        Assert.assertEquals(smsMessage.checkMessageText(TOO_LONG_TEXT_MESSAGE), SMSMessage.MessageTextState.MESSAGE_TEXT_TOO_LONG);
    }

    @Test
    public void checkMessageText_smsText_isTooLongForOne() {
        Assert.assertEquals(smsMessage.checkMessageText(MAX_SIZE_TEXT_MESSAGE_P1), SMSMessage.MessageTextState.MESSAGE_TEXT_TOO_LONG);
    }

    @Test
    public void checkMessageText_smsText_isShortEnoughForOne() {
        Assert.assertEquals(smsMessage.checkMessageText(MAX_SIZE_TEXT_MESSAGE_M1), SMSMessage.MessageTextState.MESSAGE_TEXT_VALID);
    }

    @Test
    public void checkMessageText_smsText_isValid() {
        Assert.assertEquals(smsMessage.checkMessageText(NORMAL_TEXT_MESSAGE), SMSMessage.MessageTextState.MESSAGE_TEXT_VALID);
    }
}
