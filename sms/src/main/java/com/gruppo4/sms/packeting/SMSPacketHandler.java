package com.gruppo4.sms.packeting;

import com.gruppo4.communication.packeting.PacketHandler;
import com.gruppo4.sms.dataLink.SMSPeer;

public class SMSPacketHandler implements PacketHandler<SMSPacket, SMSNetworkMessage, SMSPeer> {

    private static SMSPacketHandler instance;

    public static SMSPacketHandler getInstance() {
        if (instance == null)
            instance = new SMSPacketHandler();
        return instance;
    }

    @Override
    public SMSPacket[] parseMessage(SMSNetworkMessage message) {
        int remainingCharacters = message.getData().length() % SMSPacket.MAX_PACKET_TEXT_LEN; //This is last message length
        int packetsCount = message.getData().length() / SMSPacket.MAX_PACKET_TEXT_LEN + (remainingCharacters != 0 ? 1 : 0);
        SMSPacket[] packets = new SMSPacket[packetsCount];
        String subText;
        for (int i = 0; i < packetsCount; i++) {
            int finalCharacter = Math.min((i + 1) * SMSPacket.MAX_PACKET_TEXT_LEN, message.getData().length());
            subText = message.getData().substring(i * SMSPacket.MAX_PACKET_TEXT_LEN, finalCharacter);

            packets[i] = new SMSPacket(message.getApplicationID(), message.getMessageID(), subText, message.getDestination(), i + 1, packetsCount);
        }
        return packets;
    }

    @Override
    public SMSPacket parsePacket(Object obj, SMSPeer peer) {
        if (!(obj instanceof String))
            return null;
        String receivedData = (String) obj;

        String[] splitData = receivedData.split(SMSPacket.SEPARATOR, 5);
        if (splitData.length < 5)
            return null;

        try {
            int applicationCode = Integer.parseInt(splitData[0]);
            int messageID = Integer.parseInt(splitData[1]);
            int sequenceNumber = Integer.parseInt(splitData[2]);
            int totalPacketsNumber = Integer.parseInt(splitData[3]);
            String messageData = splitData[4];

            return new SMSPacket(applicationCode, messageID, messageData, peer, sequenceNumber, totalPacketsNumber);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
