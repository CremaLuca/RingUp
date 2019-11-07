package com.gruppo4.communication.packeting;

import com.gruppo4.communication.dataLink.Peer;

/**
 * Class that creates packets from a message and parses received data in packets
 *
 * @param <P> packet type
 * @param <M> message type
 * @author Luca Crema
 */
public interface PacketHandler<P extends Packet, M extends NetworkMessage, A extends Peer> {

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
     * @param data data to be parsed
     * @param peer sender peer
     * @return null if the data is invalid, a packet otherwise
     */
    Packet parsePacket(Object data, A peer);

}
