package com.gruppo4.communication;

import com.gruppo4.communication.listeners.ReceivedMessageListener;

public interface CommunicationHandler {

    void sendMessage(Message message);

    void addReceivedMessageListener(ReceivedMessageListener listener);

    void removeReceivedMessageListener(ReceivedMessageListener listener);

}
