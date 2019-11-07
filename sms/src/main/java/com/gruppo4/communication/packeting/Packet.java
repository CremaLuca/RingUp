package com.gruppo4.communication.packeting;

import com.gruppo4.communication.dataLink.Peer;

public abstract class Packet<D, P extends Peer> {

    protected D data;
    protected P destination;
    protected int sequenceNumber;
    protected int totalPacketsNumber;

    protected Packet(D data, P destination, int sequenceNumber, int totalPacketsNumber) {
        this.data = data;
        this.destination = destination;
        this.sequenceNumber = sequenceNumber;
        this.totalPacketsNumber = totalPacketsNumber;
    }

    public D getData() {
        return data;
    }

    public Peer getDestination() {
        return destination;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getTotalPacketsNumber() {
        return totalPacketsNumber;
    }

    /**
     * @return packet data ready to be placed in a message and be sent
     */
    public abstract D getOutput();

}
