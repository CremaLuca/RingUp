package com.gruppo4.communication;

import com.gruppo4.communication.listeners.ReceivedMessageListener;

public abstract class CommunicationHandler<T extends Message> {

    protected ReceivedMessageListener<T> receivedMessageListener;

    public abstract void sendMessage(T message);

    /**
     * Subscribes the listener to be called once a message is completely received
     * Requires Manifest.permission.RECEIVE_SMS permission
     *
     * @param listener a class that implements SMSReceivedListener
     */
    public void addReceivedMessageListener(ReceivedMessageListener<T> listener) {
        this.receivedMessageListener = listener;
    }

    /**
     * Removes the subscription to the call on message received
     */
    public void removeReceivedMessageListener() {
        this.receivedMessageListener = null;
    }

    /**
     * Calls every listener subscribed for the reception of a message
     *
     * @param message a complete message
     */
    protected abstract void callReceivedMessageListener(T message);

}
