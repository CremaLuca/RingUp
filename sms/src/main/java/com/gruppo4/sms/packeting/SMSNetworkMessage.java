package com.gruppo4.sms.packeting;

import com.gruppo4.communication.packeting.NetworkMessage;
import com.gruppo4.sms.dataLink.SMSPeer;

/**
 * Represent a message from the app that will be sent on the SMS network, and will be reconstructed on arrival
 *
 * @author Luca Crema, Mariotto Marco
 */
public class SMSNetworkMessage extends NetworkMessage<String, SMSPeer> {

    private int messageID;
    private int applicationID;
    private SMSPacket[] packets;

    /**
     *
     * @param applicationID current valid application code
     * @param messageID valid message id
     * @param data message content
     * @param destination destination/source peer
     */
    public SMSNetworkMessage(int applicationID, int messageID, String data, SMSPeer destination) {
        super(data, destination);
        this.messageID = messageID;
        this.applicationID = applicationID;
    }

    /**
     * Returns a unique sequential code for this message
     *
     * @return the message id
     */
    public int getMessageID() {
        return messageID;
    }

    /**
     * @return identifier for the current application
     */
    public int getApplicationID() {
        return applicationID;
    }

    /**
     * Splits the message in packets
     * @return array of packets ready to be sent
     */
    SMSPacket[] getPackets() {
        if (packets == null)
            packets = SMSPacketHandler.getInstance().parseMessage(this);
        return packets;
    }

    /**
     * Checks if the message has all the packets
     *
     * @return true if there are no missing packets, false otherwise
     */
    public boolean hasAllPackets() {
        for (SMSPacket packet : packets) {
            if (packet == null)
                return false;
        }
        return true;
    }

    /**
     * Adds a packet to compose this message
     *
     * @param packet add packet to packets list
     */
    void addPacket(SMSPacket packet) {
        if (packets == null) {
            packets = new SMSPacket[packet.getTotalPacketsNumber()];
            messageID = packet.getMessageID();
        } else if (packets[packet.getSequenceNumber() - 1] != null) {
            throw new IllegalStateException("There shouldn't be a packet for this message");
        }
        packets[packet.getSequenceNumber() - 1] = packet;
        if (hasAllPackets()) constructMessage();
    }

    /**
     * Builds a message from its packets
     * @return the original content of the message
     */
    private String constructMessage() {
        StringBuilder message = new StringBuilder();
        for (SMSPacket packet : packets)
            message.append(packet.getData());
        return message.toString();
    }
}
