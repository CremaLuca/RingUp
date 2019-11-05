package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Test;

public class SMSMessageUnitTest {

    SMSMessage smsMessage;
    private static final String NORMAL_TEXT_MESSAGE = "Test message";
    private final int MAX_LENGTH_TEXT_MESSAGE_EXPECTED = smsMessage.MAX_MSG_TEXT_LEN;

    @Test
    public void checkMessageText_smsText_isTooLong(){
        String textMessage = "";
        for(int i=0;i<MAX_LENGTH_TEXT_MESSAGE_EXPECTED*2;i++){
            textMessage += "a";
        }
        Assert.assertEquals(smsMessage.checkMessageText(textMessage), SMSMessage.MessageTextState.MESSAGE_TEXT_TOO_LONG);
    }

    @Test
    public void checkMessageText_smsText_isTooLongForOne() {
        String textMessage = "";
        for(int i=0;i<MAX_LENGTH_TEXT_MESSAGE_EXPECTED + 1;i++){
            textMessage += "a";
        }
        Assert.assertEquals(smsMessage.checkMessageText(textMessage), SMSMessage.MessageTextState.MESSAGE_TEXT_TOO_LONG);
    }

    @Test
    public void checkMessageText_smsText_isShortEnoughForOne() {
        String textMessage = "";
        for(int i=0;i<MAX_LENGTH_TEXT_MESSAGE_EXPECTED - 1;i++){
            textMessage += "a";
        }
        Assert.assertEquals(smsMessage.checkMessageText(textMessage), SMSMessage.MessageTextState.MESSAGE_TEXT_VALID);
    }

    @Test
    public void checkMessageText_smsText_isValid() {
        Assert.assertEquals(smsMessage.checkMessageText(NORMAL_TEXT_MESSAGE), SMSMessage.MessageTextState.MESSAGE_TEXT_VALID);
    }
}
