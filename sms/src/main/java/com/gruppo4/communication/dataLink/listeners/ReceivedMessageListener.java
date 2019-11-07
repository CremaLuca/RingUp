package com.gruppo4.communication.dataLink.listeners;

import com.gruppo4.communication.dataLink.Message;

public interface ReceivedMessageListener<T extends Message> {

    void onMessageReceived(T message);

}
