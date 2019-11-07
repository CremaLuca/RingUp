package com.gruppo4.communication.packeting;

import com.gruppo4.communication.dataLink.Peer;

/**
 * Representation of a single message for the communication channel ie. for SMS will be less than 140 bytes long
 *
 * @param <D> data type
 * @param <P> peer type
 * @author Luca Crema
 */
public abstract class Packet<D, P extends Peer> {

    protected D data;
    protected P destination;
    protected int sequenceNumber;
    protected int totalPacketsNumber;

    /**
     * @param data               content of the packet
     * @param destination        destination peer
     * @param sequenceNumber     index of this packet in the message
     * @param totalPacketsNumber total packet for the message
     */
    protected Packet(D data, P destination, int sequenceNumber, int totalPacketsNumber) {
        this.data = data;
        this.destination = destination;
        this.sequenceNumber = sequenceNumber;
        this.totalPacketsNumber = totalPacketsNumber;
    }

    /**
     * @return the content of the packet, part of the message
     */
    public D getData() {
        return data;
    }

    /**
     * @return the destination peer for the packet
     */
    public P getDestination() {
        return destination;
    }

    /**
     * @return the index of the packet in the packet list for the message
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @return total number of packets for the message
     */
    public int getTotalPacketsNumber() {
        return totalPacketsNumber;
    }

    /**
     * @return packet data ready to be placed in a message and be sent
     */
    public abstract D getOutput();

}
