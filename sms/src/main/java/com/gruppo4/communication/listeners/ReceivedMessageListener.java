package com.gruppo4.communication.listeners;

import com.gruppo4.communication.Message;

public interface ReceivedMessageListener<T extends Message> {

    void onMessageReceived(T message);

}
