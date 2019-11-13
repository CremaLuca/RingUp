package com.gruppo4.communication.dataLink;

/**
 * Handles communications in a channel
 *
 * @param <T> message class to be used for the channel
 */
public abstract class CommunicationHandler<T extends Message> {

    /**
     * Sends a single message in the channel, message content must be shorter than the maximum channel message size
     *
     * @param message message to be sent in the channel to a peer
     */
    public abstract void sendMessage(T message);

}
