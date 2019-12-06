package com.gruppo4.sms.dataLink;

import androidx.annotation.NonNull;

import com.gruppo4.communication.dataLink.Message;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.listeners.SMSDeliveredListener;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

/**
 * Representation of a single sms message
 * This class does NOT parse SMSMessages into sms-ready strings and back!
 *
 * @author Luca Crema, Marco Mariotto, Alberto Ursino
 */
public class SMSMessage implements Message<String, SMSPeer> {

    /**
     * Kind of a magic number, should be around 160 but doesn't work all the times.
     * (suggestions accepted)
     */
    public static final int MAX_MSG_TEXT_LEN = 155;
    private String messageContent;
    private SMSPeer peer;

    /**
     * Constructor for a sms text message.
     *
     * @param peer        a valid peer
     * @param messageText the message content, can be empty but not null
     * @throws InvalidSMSMessageException if checkMessageText is different from MESSAGE_TEXT_VALID
     */
    public SMSMessage(@NonNull SMSPeer peer, @NonNull String messageText) throws InvalidSMSMessageException {
        //Checks on the message text
        ContentState contentState = checkMessageText(messageText);
        if (contentState != ContentState.MESSAGE_TEXT_VALID)
            throw new InvalidSMSMessageException("Message text length exceeds maximum allowed", contentState);
        this.messageContent = messageText;
        this.peer = peer;
    }

    /**
     * Checks if the message content could be valid.
     *
     * @param messageText to be checked.
     * @return The state of the message after the validity tests.
     */
    public static ContentState checkMessageText(@NonNull String messageText) {
        if (messageText.length() > SMSMessage.MAX_MSG_TEXT_LEN) {
            return ContentState.MESSAGE_TEXT_TOO_LONG;
        }
        return ContentState.MESSAGE_TEXT_VALID;
    }

    /**
     * Retrieves the data received by or to be sent in the network.
     *
     * @return data contained in this message or to put in an sms
     */
    @Override
    public String getData() {
        return messageContent;
    }

    /**
     * Retrieves the sender or the destination for the message
     *
     * @return Peer associated with this message
     */
    @Override
    public SMSPeer getPeer() {
        return peer;
    }

    /**
     * Possible states of a message after a check
     * There could be more in future so it has been used an enum
     * <p>
     * These are used in {@link #checkMessageText}
     */
    public enum ContentState {
        MESSAGE_TEXT_VALID,
        MESSAGE_TEXT_TOO_LONG
    }

    /**
     * Possible states of a message after it has been sent
     * These are given by Android library as an int, they have been put in an enum
     * so that one can see all the possible values without having to
     * look at the official Android documentation
     * <p>
     * These are used in {@link SMSSentListener}
     */
    public enum SentState {
        MESSAGE_SENT,
        ERROR_GENERIC_FAILURE,
        ERROR_RADIO_OFF,
        ERROR_NULL_PDU,
        ERROR_NO_SERVICE,
        ERROR_LIMIT_EXCEEDED
    }

    /**
     * Possible states of a message after it has been delivered,
     * These are given by Android library as an int, they have been put in an enum so
     * that one can see al the possible values without having to look
     * at the official Android documentation
     *
     * These are used in {@link SMSDeliveredListener}
     */
    public enum DeliveredState {
        MESSAGE_DELIVERED,
        DELIVERY_ERROR,
        ERROR_GENERIC_FAILURE
    }
}



