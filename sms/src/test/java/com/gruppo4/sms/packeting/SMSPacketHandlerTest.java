package com.gruppo4.sms.packeting;

import com.gruppo4.sms.dataLink.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SMSPacketHandlerTest {

    private static final int DEFAULT_APP_CODE = 5;
    private static final int DEFAULT_MESSAGE_ID = 50;
    private static final String DEFAULT_MESSAGE_CONTENT = "Ciao! I'm Luca!";
    private static final String LONG_MESSAGE_CONTENT = "Ciao! My name is Luca and this string must become at least 160 characters long in order to have it split in two different packets by the parser. This is not enough so I'll go on and make it a little longer";
    private static final SMSPeer DEFAULT_PEER = new SMSPeer("+393467965447");
    private static final int DEFAULT_PACKET_SEQUENCE_NUMBER = 3;
    private static final int DEFAULT_PACKET_TOTAL_PACKETS_NUMBER = 5;
    SMSPacketHandler handlerInstance;

    @Before
    public void init() {
        handlerInstance = SMSPacketHandler.getInstance();
    }

    @Test
    public void parseMessage_numberOfPackets_isEquals() {
        SMSNetworkMessage testMessage = new SMSNetworkMessage(DEFAULT_APP_CODE, DEFAULT_MESSAGE_ID, DEFAULT_MESSAGE_CONTENT, DEFAULT_PEER);
        Assert.assertEquals(1, handlerInstance.parseMessage(testMessage).length);
    }

    @Test
    public void parseMessage_numberOfPackets_isEqualsWhenLong() {
        SMSNetworkMessage testMessage = new SMSNetworkMessage(DEFAULT_APP_CODE, DEFAULT_MESSAGE_ID, LONG_MESSAGE_CONTENT, DEFAULT_PEER);
        Assert.assertEquals(2, handlerInstance.parseMessage(testMessage).length);
    }

    @Test
    public void parsePacket_SMSPacket_isEquals() {
        SMSPacket testPacket = new SMSPacket(DEFAULT_APP_CODE, DEFAULT_MESSAGE_ID, DEFAULT_MESSAGE_CONTENT, DEFAULT_PEER, DEFAULT_PACKET_SEQUENCE_NUMBER, DEFAULT_PACKET_TOTAL_PACKETS_NUMBER);
        String output = testPacket.getOutput();
        SMSPeer peer = testPacket.getDestination();
        Assert.assertEquals(testPacket, handlerInstance.parsePacket(output, peer));
    }

    @Test
    public void parsePacket_SMSPacket_isNotEquals() {
        SMSPacket testPacket = new SMSPacket(DEFAULT_APP_CODE, DEFAULT_MESSAGE_ID, DEFAULT_MESSAGE_CONTENT, DEFAULT_PEER, DEFAULT_PACKET_SEQUENCE_NUMBER, DEFAULT_PACKET_TOTAL_PACKETS_NUMBER);
        String output = testPacket.getOutput();
        SMSPeer peer = testPacket.getDestination();

        SMSPacket testPacketNotEquals = new SMSPacket(DEFAULT_APP_CODE + 1, DEFAULT_MESSAGE_ID, DEFAULT_MESSAGE_CONTENT, DEFAULT_PEER, DEFAULT_PACKET_SEQUENCE_NUMBER, DEFAULT_PACKET_TOTAL_PACKETS_NUMBER);

        Assert.assertNotEquals(testPacketNotEquals, handlerInstance.parsePacket(output, peer));
    }
}
