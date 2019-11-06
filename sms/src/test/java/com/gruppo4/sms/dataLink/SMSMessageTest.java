package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SMSMessageTest extends Variables {

    @Before
    public void init_onePacket() {
        try {
            smsMessage = new SMSMessage(new SMSPeer(VALID_TELEPHONE_NUMBER), SMS_PACKET);
        } catch (Exception e) {
            Assert.fail(SHOULD_NOT);
        }
    }

    @Before
    public void init_morePackets() {
        SMSPacket[] smsPackets = {SMS_PACKET, SMS_PACKET_2};
        try {
            smsMessage = new SMSMessage(new SMSPeer(VALID_TELEPHONE_NUMBER), smsPackets);
        } catch (Exception e) {
            Assert.fail(SHOULD_NOT);
        }
    }

    @Test
    public void getPacketsContent_packetsContent_areEquals() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(VALID_TEXT_MESSAGE);
        Assert.assertEquals(VALID_PACKET_DATA, SMS_MESSAGE.getPacketsContent().toString());
    }

    @Test
    public void getPackets_packets_areEquals() {
        SMSPacket[] smsPackets = {SMS_PACKET, SMS_PACKET_2};
        smsMessage = new SMSMessage(new SMSPeer(VALID_TELEPHONE_NUMBER), smsPackets);
        Assert.assertEquals(smsMessage.getPackets(), smsPackets);
    }

    @Test
    public void checkMessageText_smsText_isTooLong() {
        Assert.assertEquals(smsMessage.checkMessageText(TOO_LONG_TEXT_MESSAGE), SMSMessage.MessageTextState.MESSAGE_TEXT_TOO_LONG);
    }

    @Test
    public void checkMessageText_smsText_isTooLongByOne() {
        Assert.assertEquals(smsMessage.checkMessageText(MAX_LENGTH_TEXT_MESSAGE_P1), SMSMessage.MessageTextState.MESSAGE_TEXT_TOO_LONG);
    }

    @Test
    public void checkMessageText_smsText_isMaxLength() {
        Assert.assertEquals(smsMessage.checkMessageText(MAX_LENGTH_TEXT_MESSAGE), SMSMessage.MessageTextState.MESSAGE_TEXT_VALID);
    }

    @Test
    public void checkMessageText_smsText_isValid() {
        Assert.assertEquals(smsMessage.checkMessageText(VALID_TEXT_MESSAGE), SMSMessage.MessageTextState.MESSAGE_TEXT_VALID);
    }

    @Test
    public void checkMessageText_smsText_isEmpty() {
        Assert.assertEquals(smsMessage.checkMessageText(EMPTY_TEXT_MESSAGE), SMSMessage.MessageTextState.MESSAGE_TEXT_VALID);
    }

    @Test
    public void getPeer_smsPeer_isEquals() {
        Assert.assertEquals(SMS_MESSAGE.getPeer().toString(), VALID_TELEPHONE_NUMBER);
    }

    @Test
    public void getData_smsData_isEquals() {
        Assert.assertEquals(SMS_MESSAGE.getData(), VALID_TEXT_MESSAGE);
    }

    @Test
    public void getMessageId_messageId_isEquals() {
        Assert.assertEquals(VALID_MESSAGE_ID, SMS_MESSAGE.getMessageId());
    }
}
