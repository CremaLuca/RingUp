package com.gruppo4.communication;

public interface Message<D, P extends Peer> {

    D getData();

    P getPeer();

}
