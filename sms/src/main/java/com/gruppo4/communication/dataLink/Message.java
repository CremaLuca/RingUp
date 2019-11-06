package com.gruppo4.communication.dataLink;

public abstract class Message<D, P extends Peer> {

    protected D data;
    protected P peer;

    /**
     * Constructor
     *
     * @param data valid data
     * @param peer valid peer
     */
    public Message(D data, P peer) {
        this.data = data;
        this.peer = peer;
    }

    public D getData() {
        return data;
    }

    /**
     * telephoneNumber is either the number from which the message comes from, or the number where to send the message
     *
     * @return the telephone number
     */
    public P getPeer() {
        return peer;
    }

}
