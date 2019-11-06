package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Test;

public class SMSPacketTest extends Variables {

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
            new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, MAX_LENGTH_PACKET_MESSAGE_P1);
            Assert.fail(SHOULD_THROW);
        } catch (Exception e) {
            //Success
        }
    }

    @Test
    public void message_isMaxLength() {
        try {
            new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, MAX_LENGTH_PACKET_MESSAGE);
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
