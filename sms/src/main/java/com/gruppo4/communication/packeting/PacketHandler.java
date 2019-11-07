package com.gruppo4.communication.packeting;

public interface PacketHandler<P extends Packet, M extends NetworkMessage> {

    /**
     * Parses a bigger message into small packets
     *
     * @param message the message
     * @return an array of packets ready to be sent
     */
    P[] parseMessage(M message);

    /**
     * Parses received data into a packet
     *
     * @param o data to be parsed
     * @return null if the data is invalid, a packet otherwise
     */
    Packet parsePacket(Object o);

}
