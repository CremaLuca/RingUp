package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Test;

public class SMSPacketTest {

    private static final String VALID_TEXT_MESSAGE = "Test message";
    private static final int MAX_PACKET_TEXT_LEN = 140;
    private static final int VALID_APPLICATION_CODE = 1;
    private static final int VALID_MESSAGE_ID = 1;
    private static final int VALID_PACKET_NUMBER = 1;
    private static final int VALID_TOTAL_PACKET_NUMBER = 1;
    private static final int TOO_BIG_APPLICATION_CODE = 1000;
    private static final int TOO_BIG_MESSAGE_ID = 1000;
    private static final int TOO_BIG_PACKET_NUMBER = 1000;
    private static final int TOO_BIG_TOTAL_PACKET_NUMBER = 1000;
    private static final String SHOULD_THROW = "Should have thrown an exception";
    private static final String TOO_LONG_TEXT_MESSAGE = new String(new char[MAX_PACKET_TEXT_LEN * 2]).replace('\0', ' ');
    private static final String TOO_LONG_TEXT_MESSAGE_P1 = new String(new char[MAX_PACKET_TEXT_LEN + 1]).replace('\0', ' ');  //P1 = Plus 1
    private static final String MAX_LENGTH_TEXT_MESSAGE = new String(new char[MAX_PACKET_TEXT_LEN]).replace('\0', ' ');
    private static final SMSPacket SMS_PACKET = new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE);

    @Test
    public void application_code_isEquals() {
        Assert.assertEquals(SMS_PACKET.getApplicationCode(), VALID_APPLICATION_CODE);
    }

    @Test
    public void message_code_isEquals() {
        Assert.assertEquals(SMS_PACKET.getApplicationCode(), VALID_APPLICATION_CODE);
    }

    @Test
    public void packet_number_isEquals() {
        Assert.assertEquals(SMS_PACKET.getPacketNumber(), VALID_PACKET_NUMBER);
    }

    @Test
    public void total_number_isEquals() {
        Assert.assertEquals(SMS_PACKET.getTotalNumber(), VALID_TOTAL_PACKET_NUMBER);
    }

    @Test
    public void message_isEquals() {
        Assert.assertEquals(SMS_PACKET.getMessageText(), VALID_TEXT_MESSAGE);
    }

    @Test
    public void application_code_tooBig() {
        try {
            new SMSPacket(TOO_BIG_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE);
            Assert.fail(SHOULD_THROW);
        } catch (Exception e) {
            //Success
        }
    }

    @Test
    public void message_code_tooBig() {
        try {
            new SMSPacket(VALID_APPLICATION_CODE, TOO_BIG_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE);
            Assert.fail(SHOULD_THROW);
        } catch (Exception e) {
            //Success
        }
    }

    @Test
    public void packet_number_tooBig() {
        try {
            new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, TOO_BIG_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE);
            Assert.fail(SHOULD_THROW);
        } catch (Exception e) {
            //Success
        }
    }

    @Test
    public void total_number_tooBig() {
        try {
            new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, TOO_BIG_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE);
            Assert.fail(SHOULD_THROW);
        } catch (Exception e) {
            //Success
        }
    }

    @Test
    public void message_tooLong() {
        try {
            new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, TOO_LONG_TEXT_MESSAGE);
            Assert.fail(SHOULD_THROW);
        } catch (Exception e) {
            //Success
        }
    }

    @Test
    public void message_tooLongByOne() {
        try {
            new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, TOO_LONG_TEXT_MESSAGE_P1);
            Assert.fail(SHOULD_THROW);
        } catch (Exception e) {
            //Success
        }
    }

    @Test
    public void message_isMaxLength() {
        try {
            new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, MAX_LENGTH_TEXT_MESSAGE);
        } catch (Exception e) {
            Assert.fail(SHOULD_THROW);
        }
    }

    /**
     * Checks if an exception is thrown if the packet number is greater than the total number
     */
    @Test
    public void packet_numbering_isWrong() {
        try {
            new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER + 1, VALID_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE);
            Assert.fail(SHOULD_THROW);
        } catch (Exception e) {
            //Success
        }
    }
}
