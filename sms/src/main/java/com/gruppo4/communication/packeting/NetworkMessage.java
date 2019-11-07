package com.gruppo4.communication.packeting;

import com.gruppo4.communication.dataLink.Peer;

/**
 * Message to be divided in packets and be sent via network
 * Will probably change name and be moved somewhere else
 *
 * @param <D> data type
 * @param <P> peer type
 * @author Luca Crema
 */
public abstract class NetworkMessage<D, P extends Peer> {

    protected D data;
    protected P destination;

    /**
     * @param data        content of the message
     * @param destination destination of the message
     */
    public NetworkMessage(D data, P destination) {
        this.data = data;
        this.destination = destination;
    }

    /**
     * @return the content of the message to be split into packets
     */
    public D getData() {
        return data;
    }

    /**
     * @return peer destination of the message
     */
    public P getDestination() {
        return destination;
    }

}
