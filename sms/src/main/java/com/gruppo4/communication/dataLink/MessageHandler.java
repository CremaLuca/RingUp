package com.gruppo4.communication.dataLink;

/**
 * Class used to switch from actual communication data to Message and back
 *
 * @param <M> The type of message to parse and output
 */
public abstract class MessageHandler<M extends Message> {

    /**
     * Interprets a string arrived via the communication channel and parses it to a message
     *
     * @param data data from the communication channel
     * @return the message if the string has been parsed correctly, null otherwise
     */
    protected abstract M parseMessage(String data, String peerData);

    /**
     * Translates a message into a string that can be sent via communication channel
     *
     * @param message message to be translated
     * @return the string to send
     */
    protected abstract String getOutput(M message);
}
