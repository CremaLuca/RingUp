package com.gruppo4.sms.packeting;

import com.gruppo4.sms.dataLink.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SMSPacketUnitTest {

    private static final int DEFAULT_APPLICATION_CODE = 987;
    private static final int DEFAULT_MESSAGE_ID = 60;
    private static final String DEFAULT_MESSAGE = "Ciao, test!";
    private static final SMSPeer DEFAULT_PEER = new SMSPeer("+393467965447");
    private static final int DEFAULT_SEQUENCE_NUMBER = 3;
    private static final int DEFAULT_TOTAL_PACKETS_NUMBER = 5;
    private static final String SEPARATOR = SMSPacket.SEPARATOR;

    private SMSPacket testPacket;

    @Before
    public void init() {
        testPacket = new SMSPacket(DEFAULT_APPLICATION_CODE, DEFAULT_MESSAGE_ID, DEFAULT_MESSAGE, DEFAULT_PEER, DEFAULT_SEQUENCE_NUMBER, DEFAULT_TOTAL_PACKETS_NUMBER);
    }

    @Test
    public void getMessageID_messageID_isEquals() {
        Assert.assertEquals(testPacket.getMessageID(), DEFAULT_MESSAGE_ID);
    }

    @Test
    public void getApplicationCode_applicationCode_isEquals() {
        Assert.assertEquals(testPacket.getApplicationCode(), DEFAULT_APPLICATION_CODE);
    }

    @Test
    public void getOutput_output_isEquals() {
        String expected = DEFAULT_APPLICATION_CODE + SEPARATOR + DEFAULT_MESSAGE_ID + SEPARATOR + DEFAULT_SEQUENCE_NUMBER + SEPARATOR + DEFAULT_TOTAL_PACKETS_NUMBER + SEPARATOR + DEFAULT_MESSAGE;
        Assert.assertEquals(testPacket.getOutput(), expected);
    }

    @Test
    public void equals_applicationCode_isNotEquals() {
        SMSPacket wrongTestPacket = new SMSPacket(DEFAULT_APPLICATION_CODE + 1, DEFAULT_MESSAGE_ID, DEFAULT_MESSAGE, DEFAULT_PEER, DEFAULT_SEQUENCE_NUMBER, DEFAULT_TOTAL_PACKETS_NUMBER);
        Assert.assertNotEquals(wrongTestPacket, testPacket);
    }

    @Test
    public void equals_messageID_isNotEquals() {
        SMSPacket wrongTestPacket = new SMSPacket(DEFAULT_APPLICATION_CODE, DEFAULT_MESSAGE_ID + 1, DEFAULT_MESSAGE, DEFAULT_PEER, DEFAULT_SEQUENCE_NUMBER, DEFAULT_TOTAL_PACKETS_NUMBER);
        Assert.assertNotEquals(wrongTestPacket, testPacket);
    }

    @Test
    public void equals_messageContent_isNotEquals() {
        SMSPacket wrongTestPacket = new SMSPacket(DEFAULT_APPLICATION_CODE, DEFAULT_MESSAGE_ID, DEFAULT_MESSAGE + "!", DEFAULT_PEER, DEFAULT_SEQUENCE_NUMBER, DEFAULT_TOTAL_PACKETS_NUMBER);
        Assert.assertNotEquals(wrongTestPacket, testPacket);
    }

    @Test
    public void equals_peer_isNotEquals() {
        SMSPacket wrongTestPacket = new SMSPacket(DEFAULT_APPLICATION_CODE, DEFAULT_MESSAGE_ID, DEFAULT_MESSAGE, new SMSPeer("+393331934099"), DEFAULT_SEQUENCE_NUMBER, DEFAULT_TOTAL_PACKETS_NUMBER);
        Assert.assertNotEquals(wrongTestPacket, testPacket);
    }

    @Test
    public void equals_sequenceNumber_isNotEquals() {
        SMSPacket wrongTestPacket = new SMSPacket(DEFAULT_APPLICATION_CODE, DEFAULT_MESSAGE_ID, DEFAULT_MESSAGE, DEFAULT_PEER, DEFAULT_SEQUENCE_NUMBER + 1, DEFAULT_TOTAL_PACKETS_NUMBER);
        Assert.assertNotEquals(wrongTestPacket, testPacket);
    }

    @Test
    public void equals_totalPacketsNumber_isNotEquals() {
        SMSPacket wrongTestPacket = new SMSPacket(DEFAULT_APPLICATION_CODE, DEFAULT_MESSAGE_ID, DEFAULT_MESSAGE, DEFAULT_PEER, DEFAULT_SEQUENCE_NUMBER, DEFAULT_TOTAL_PACKETS_NUMBER + 1);
        Assert.assertNotEquals(wrongTestPacket, testPacket);
    }

    @Test
    public void equal_class_isNotEquals() {
        Object wrongTestPacket = new Object();
        Assert.assertNotEquals(wrongTestPacket, testPacket);
    }
}
