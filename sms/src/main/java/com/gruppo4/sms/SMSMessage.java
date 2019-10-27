package com.gruppo4.sms;

import android.util.Log;
import com.gruppo4.sms.utils.SMSChecks;
import com.gruppo4.sms.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.exceptions.InvalidTelephoneNumberException;

public class SMSMessage {

    public enum MessageTextState {
        MESSAGE_TEXT_VALID,
        MESSAGE_TEXT_TOO_LONG
    }

    //This is because package number cannot exceed three characters
    public static final int MAX_MSG_TEXT_LEN = SMSPacket. MAX_PACKET_TEXT_LEN  * 999; //we deliver at most 999 packets
    private String telephoneNumber;
    private String text;
    private int messageCode;

    /**
     * Wrap for a text message, used to check the parameters validity
     *
     * @param telephoneNumber a valid telephone number to send the message to
     * @param messageText     the text to be sent via one or multiple SMS depending on its length
     * @param messageCode     code for this message
     * @throws InvalidSMSMessageException      if Utils.checkMessageText return false
     * @throws InvalidTelephoneNumberException if Utils.checkTelephoneNumber return false
     */
    public SMSMessage(String telephoneNumber, String messageText, int messageCode)
            throws InvalidSMSMessageException, InvalidTelephoneNumberException {

        SMSChecks.TelephoneNumberState telephoneNumberState = SMSChecks.checkTelephoneNumber(telephoneNumber);
        if (telephoneNumberState == SMSChecks.TelephoneNumberState.TELEPHONE_NUMBER_VALID)
            this.telephoneNumber = telephoneNumber;
        else
            throw new InvalidTelephoneNumberException("Telephone number invalid, reason: " + telephoneNumberState, telephoneNumberState);

        MessageTextState messageTextState = SMSChecks.checkMessageText(messageText);
        if (messageTextState == MessageTextState.MESSAGE_TEXT_VALID)
            this.text = messageText;
        else
            throw new InvalidSMSMessageException("The message text is invalid, reason: " + messageTextState, messageTextState);

        if (!SMSChecks.checkMessageCode(messageCode))
            throw new IllegalArgumentException("Message code is out of bounds");

        this.messageCode = messageCode;
    }

    /**
     * Telephone Number is the number this message has to be sent to or has been already sent
     *
     * @return the telephone number
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Message Text is the content of the message that can be sent via one or multiple SMS
     *
     * @return the message
     */
    public String getText() {
        return text;
    }

    /**
     * Message Code is an identifier for the service that will send or use the message content
     * Also it helps to identify packets belonging to the same message
     * @return the message code
     */
    public int getCode() {
        return messageCode;
    }
}
