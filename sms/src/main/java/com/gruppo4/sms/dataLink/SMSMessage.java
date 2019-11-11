package com.gruppo4.sms.dataLink;

import android.content.Context;

import com.gruppo4.communication.dataLink.Message;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;

public class SMSMessage extends Message<String, SMSPeer> {

    public static final int MAX_MSG_TEXT_LEN = 155;
    private int applicationID;

    /**
     * Wrap for a text message, used to check the parameters validity
     *
     * @param applicationID the current valid application identifier
     * @param peer          a valid peer
     * @param messageText   the message content
     * @throws InvalidSMSMessageException      if checkMessageText is different from MESSAGE_TEXT_VALID
     * @throws InvalidTelephoneNumberException if SMSPeer.checkPhoneNumber() is different from TELEPHONE_NUMBER_VALID
     */
    public SMSMessage(int applicationID, SMSPeer peer, String messageText) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        super(messageText, peer);
        //Checks on the peer
        SMSPeer.TelephoneNumberState telephoneNumberState = peer.checkPhoneNumber();
        if (telephoneNumberState != SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_VALID) {
            throw new InvalidTelephoneNumberException("Telephone number not valid", telephoneNumberState);
        }
        //Checks on the message text
        MessageTextState messageTextState = checkMessageText(messageText);
        if (messageTextState != MessageTextState.MESSAGE_TEXT_VALID)
            throw new InvalidSMSMessageException("text length exceeds maximum allowed", messageTextState);
        this.applicationID = applicationID;
    }

    /**
     * Overload for constructor
     *
     * @param destination a valid peer address
     * @param messageText the message
     * @throws InvalidSMSMessageException      if checkMessageText is different from MESSAGE_TEXT_VALID
     * @throws InvalidTelephoneNumberException if SMSPeer.checkPhoneNumber() is different from TELEPHONE_NUMBER_VALID
     */
    public SMSMessage(int applicationID, String destination, String messageText) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        this(applicationID, new SMSPeer(destination), messageText);
    }

    /**
     * Overload for constructor, useful if you don't know the app code
     *
     * @param context     the current context
     * @param destination a valid peer address
     * @param messageText the message
     * @throws InvalidSMSMessageException      if checkMessageText is different from MESSAGE_TEXT_VALID
     * @throws InvalidTelephoneNumberException if SMSPeer.checkPhoneNumber() is different from TELEPHONE_NUMBER_VALID
     */
    public SMSMessage(Context context, String destination, String messageText) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        this(SMSHandler.getInstance(context).getApplicationCode(), new SMSPeer(destination), messageText);
    }

    /**
     * Checks if the message is valid
     *
     * @param messageText the text to check
     * @return The state of the message after the tests
     */
    public static MessageTextState checkMessageText(String messageText) {
        if (messageText.length() > SMSMessage.MAX_MSG_TEXT_LEN) {
            return MessageTextState.MESSAGE_TEXT_TOO_LONG;
        }
        return MessageTextState.MESSAGE_TEXT_VALID;
    }

    public int getApplicationID() {
        return applicationID;
    }

    public enum MessageTextState {
        MESSAGE_TEXT_VALID,
        MESSAGE_TEXT_TOO_LONG
    }

    public enum SentState {
        MESSAGE_SENT,
        ERROR_GENERIC_FAILURE,
        ERROR_RADIO_OFF,
        ERROR_NULL_PDU,
        ERROR_NO_SERVICE,
        ERROR_LIMIT_EXCEEDED
    }
}
