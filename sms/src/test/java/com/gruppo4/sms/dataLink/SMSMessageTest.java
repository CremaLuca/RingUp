package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Test;

public class SMSMessageTest extends Variables {

    @Test
    public void checkMessageText_smsText_isTooLong() {
        Assert.assertEquals(SMSMessage.checkMessageText(TOO_LONG_TEXT_MESSAGE), SMSMessage.MessageTextState.MESSAGE_TEXT_TOO_LONG);
    }

    @Test
    public void checkMessageText_smsText_isTooLongByOne() {
        Assert.assertEquals(SMSMessage.checkMessageText(MAX_LENGTH_TEXT_MESSAGE_P1), SMSMessage.MessageTextState.MESSAGE_TEXT_TOO_LONG);
    }

    @Test
    public void checkMessageText_smsText_isMaxLength() {
        Assert.assertEquals(SMSMessage.checkMessageText(MAX_LENGTH_TEXT_MESSAGE), SMSMessage.MessageTextState.MESSAGE_TEXT_VALID);
    }

    @Test
    public void checkMessageText_smsText_isValid() {
        Assert.assertEquals(SMSMessage.checkMessageText(VALID_TEXT_MESSAGE), SMSMessage.MessageTextState.MESSAGE_TEXT_VALID);
    }

    @Test
    public void checkMessageText_smsText_isEmpty() {
        Assert.assertEquals(SMSMessage.checkMessageText(EMPTY_TEXT_MESSAGE), SMSMessage.MessageTextState.MESSAGE_TEXT_VALID);
    }

    @Test
    public void getPeer_smsPeer_isEquals() {
        Assert.assertEquals(SMS_MESSAGE.getPeer().toString(), VALID_TELEPHONE_NUMBER);
    }

    @Test
    public void getData_smsData_isEquals() {
        Assert.assertEquals(SMS_MESSAGE.getData(), VALID_TEXT_MESSAGE);
    }

}
