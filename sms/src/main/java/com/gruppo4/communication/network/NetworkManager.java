package com.gruppo4.communication.network;

import com.gruppo4.communication.dataLink.Message;
import com.gruppo4.communication.dataLink.Peer;
import com.gruppo4.communication.dataLink.listeners.ReceivedMessageListener;

public interface NetworkManager<U extends Peer, T extends Message, RK, RV>
        extends ReceivedMessageListener<T>
{
    void invite(U user);
    void disconnect();
    void setResource(RK key, RV value);
    void removeResource(RK key);
}
