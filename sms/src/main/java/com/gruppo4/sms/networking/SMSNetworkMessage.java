package com.gruppo4.sms.networking;

import com.gruppo4.communication.networking.NetworkMessage;
import com.gruppo4.sms.dataLink.SMSPeer;

public class SMSNetworkMessage extends NetworkMessage<String, SMSPeer> {

    private int messageID;
    private int applicationID;
    private SMSPacket[] packets;

    public SMSNetworkMessage(int applicationID, int messageID, String data, SMSPeer destination) {
        super(data, destination);
        this.messageID = messageID;
        this.applicationID = applicationID;
    }

    public int getMessageID() {
        return messageID;
    }

    public int getApplicationID() {
        return applicationID;
    }

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

    private String constructMessage() {
        StringBuilder message = new StringBuilder();
        for (SMSPacket packet : packets)
            message.append(packet.getData());
        return message.toString();
    }
}
