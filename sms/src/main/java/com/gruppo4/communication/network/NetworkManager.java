package com.gruppo4.communication.network;

import com.gruppo4.communication.dataLink.Message;
import com.gruppo4.communication.dataLink.Peer;
import com.gruppo4.communication.dataLink.listeners.ReceivedMessageListener;

public interface NetworkManager<P extends Peer, V extends Resource, T extends Message>
        extends ReceivedMessageListener<T>
{
    void invite(P peer);
    void disconnect();
    void addResource(V resource);
    void removeResource(V resource);
}
