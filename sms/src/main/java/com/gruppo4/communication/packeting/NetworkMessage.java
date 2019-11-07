package com.gruppo4.communication.packeting;

import com.gruppo4.communication.dataLink.Peer;

public abstract class NetworkMessage<D, P extends Peer> {

    protected D data;
    protected P destination;

    public NetworkMessage(D data, P destination) {
        this.data = data;
        this.destination = destination;
    }

    public D getData() {
        return data;
    }

    public P getDestination() {
        return destination;
    }

}
