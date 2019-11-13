package com.gruppo4.communication.dataLink;

public abstract class CommunicationHandler<T extends Message> {

    public abstract void sendMessage(T message);

}
