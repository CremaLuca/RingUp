package com.gruppo4.sms.networking;

import com.gruppo4.communication.networking.Packet;
import com.gruppo4.communication.networking.PacketHandler;

public class SMSPacketHandler implements PacketHandler<SMSPacket, SMSNetworkMessage> {

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
    public Packet parsePacket(Object o) {
        return null;
    }
}
