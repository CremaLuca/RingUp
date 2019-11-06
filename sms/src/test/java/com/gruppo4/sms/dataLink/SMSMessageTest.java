package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SMSMessageTest {

    private SMSMessage smsMessage = null;
    private static final String VALID_TEXT_MESSAGE = "Test message";
    private static final int MAX_MSG_TEXT_LEN = 139860;
    private static final String TOO_LONG_TEXT_MESSAGE = new String(new char[MAX_MSG_TEXT_LEN * 2]).replace('\0', ' ');
    private static final String MAX_LENGTH_TEXT_MESSAGE_P1 = new String(new char[MAX_MSG_TEXT_LEN + 1]).replace('\0', ' ');
    private static final String MAX_LENGTH_TEXT_MESSAGE = new String(new char[MAX_MSG_TEXT_LEN]).replace('\0', ' ');
    private static final String EMPTY_TEXT_MESSAGE = "";
    private static final String SHOULD_NOT = "Should not have thrown an exception";
    private static final String VALID_TELEPHONE_NUMBER = "+391111111111";
    private static final int VALID_APPLICATION_CODE = 1;
    private static final int VALID_MESSAGE_ID = 1;
    private static final int VALID_PACKET_NUMBER = 1;
    private static final int VALID_TOTAL_PACKET_NUMBER = 1;
    private static final String VALID_PACKET_DATA = "[1_1_1_1_Test message]"; //Used VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE
    private static final SMSPacket SMS_PACKET = new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE);
    private static final SMSPacket SMS_PACKET_2 = new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE);


    @Test
    public void constructorOnePacket_parameters_areValid() {
        try {
            smsMessage = new SMSMessage(new SMSPeer(VALID_TELEPHONE_NUMBER), SMS_PACKET);
        } catch (Exception e) {
            Assert.fail(SHOULD_NOT);
        }
    }

    @Test
    public void constructorMorePackets_parameters_areValid() {
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
        smsMessage = new SMSMessage(new SMSPeer(VALID_TELEPHONE_NUMBER), SMS_PACKET);
        Assert.assertEquals(VALID_PACKET_DATA, smsMessage.getPacketsContent().toString());
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
        Assert.assertEquals(new SMSMessage(new SMSPeer(VALID_TELEPHONE_NUMBER), SMS_PACKET).getPeer().toString(), VALID_TELEPHONE_NUMBER);
    }

    @Test
    public void getData_smsData_isEquals() {
        Assert.assertEquals(new SMSMessage(new SMSPeer(VALID_TELEPHONE_NUMBER), SMS_PACKET).getData(), VALID_TEXT_MESSAGE);
    }

    @Test
    public void getMessageId_messageId_isEquals() {
        Assert.assertEquals(VALID_MESSAGE_ID, new SMSMessage(new SMSPeer(VALID_TELEPHONE_NUMBER), SMS_PACKET).getMessageId());
    }
}
