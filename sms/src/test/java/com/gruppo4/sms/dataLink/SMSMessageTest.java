package com.gruppo4.sms.dataLink;

import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alberto Ursino
 */
public class SMSMessageTest extends Variables {

    private static SMSMessage smsMessage;
    protected static final String TOO_LONG_TEXT_MESSAGE = new String(new char[MAX_MSG_TEXT_LEN * 2]).replace('\0', ' ');
    protected static final String MAX_LENGTH_TEXT_MESSAGE = new String(new char[MAX_MSG_TEXT_LEN]).replace('\0', ' ');
    protected static final String MAX_LENGTH_TEXT_MESSAGE_P1 = new String(new char[MAX_MSG_TEXT_LEN + 1]).replace('\0', ' '); //P1 = Plus 1
    protected static final String EMPTY_TEXT_MESSAGE = VALID_MESSAGE_START+"";

    @Before
    public void init() {
        smsMessage = new SMSMessage(VALID_APPLICATION_CODE, new SMSPeer(VALID_TELEPHONE_NUMBER), VALID_TEXT_MESSAGE);
    }

    @Test
    public void smsMessage_2ndConstructor_isOk() {
        try {
            smsMessage = new SMSMessage(VALID_APPLICATION_CODE, VALID_TELEPHONE_NUMBER, VALID_TEXT_MESSAGE);
        } catch (Exception e) {
            Assert.fail(SHOULD_NOT);
        }
    }

    @Test
    public void smsMessage_constructor_notValidTextMessage() {
        try {
            smsMessage = new SMSMessage(VALID_APPLICATION_CODE, new SMSPeer(VALID_TELEPHONE_NUMBER), TOO_LONG_TEXT_MESSAGE);
            Assert.fail(SHOULD_THROW);
        } catch (InvalidSMSMessageException e) {
            //Success
        }
    }

    @Test
    public void smsMessage_constructor_notValidTelephoneNum() {
        try {
            smsMessage = new SMSMessage(VALID_APPLICATION_CODE, new SMSPeer(WRONG_TELEPHONE_NUMBER), VALID_TEXT_MESSAGE);
            Assert.fail(SHOULD_NOT);
        } catch (InvalidTelephoneNumberException e) {
            //Success
        }
    }

    @Test
    public void smsMessage_2ndConstructor_notValidTextMessage() {
        try {
            smsMessage = new SMSMessage(VALID_APPLICATION_CODE, VALID_TELEPHONE_NUMBER, TOO_LONG_TEXT_MESSAGE);
            Assert.fail(SHOULD_NOT);
        } catch (InvalidSMSMessageException e) {
            //Success
        }
    }

    @Test
    public void smsMessage_2ndConstructor_notValidTelephoneNum() {
        try {
            smsMessage = new SMSMessage(VALID_APPLICATION_CODE, WRONG_TELEPHONE_NUMBER, VALID_TEXT_MESSAGE);
            Assert.fail(SHOULD_NOT);
        } catch (InvalidTelephoneNumberException e) {
            //Success
        }
    }

    @Test
    public void checkMessageText_smsText_isTooLong() {
        Assert.assertEquals(SMSMessage.MessageTextState.MESSAGE_TEXT_TOO_LONG, SMSMessage.checkMessageText(TOO_LONG_TEXT_MESSAGE));
    }

    @Test
    public void checkMessageText_smsText_isTooLongByOne() {
        Assert.assertEquals(SMSMessage.MessageTextState.MESSAGE_TEXT_TOO_LONG, SMSMessage.checkMessageText(MAX_LENGTH_TEXT_MESSAGE_P1));
    }

    @Test
    public void checkMessageText_smsText_isMaxLength() {
        Assert.assertEquals(SMSMessage.MessageTextState.MESSAGE_TEXT_VALID, SMSMessage.checkMessageText(MAX_LENGTH_TEXT_MESSAGE));
    }

    @Test
    public void checkMessageText_smsText_isValid() {
        Assert.assertEquals(SMSMessage.MessageTextState.MESSAGE_TEXT_VALID, SMSMessage.checkMessageText(VALID_TEXT_MESSAGE));
    }

    @Test
    public void checkMessageText_smsText_isEmpty() {
        Assert.assertEquals(SMSMessage.MessageTextState.MESSAGE_TEXT_VALID, SMSMessage.checkMessageText(EMPTY_TEXT_MESSAGE));
    }

    @Test
    public void getPeer_smsPeer_isEquals() {
        Assert.assertEquals(VALID_TELEPHONE_NUMBER, smsMessage.getPeer().toString());
    }

    @Test
    public void getData_smsData_isEquals() {
        Assert.assertEquals(VALID_TEXT_MESSAGE, smsMessage.getData());
    }

    @Test
    public void getApplicationID_appID_areEquals() {
        Assert.assertEquals(smsMessage.getApplicationID(), VALID_APPLICATION_CODE);
    }
}
