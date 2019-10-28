package com.gruppo4.sms;

import com.gruppo4.sms.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.utils.SMSChecks;

public class SMSMessage {

    //This is because package number cannot exceed three characters
    public static  final  int MAX_PACKETS = 999;
    public static final int MAX_MSG_TEXT_LEN = SMSPacket.MAX_PACKET_TEXT_LEN * MAX_PACKETS; //we deliver at most 999 packets
    private String telephoneNumber;
    private String message;

    private SMSPacket[] packets;

    /**
     * Wrap for a text message, used to check the parameters validity
     *
     * @param telephoneNumber a valid telephone number to send the message to
     * @param packets     array of packets from which we construct the message
     * @throws InvalidSMSMessageException      if Utils.checkMessageText return false
     * @throws InvalidTelephoneNumberException if Utils.checkTelephoneNumber return false
     */

    public SMSMessage(String telephoneNumber, SMSPacket[] packets)
    {
        this.telephoneNumber = telephoneNumber;
        this.packets = packets;
        for (SMSPacket p: packets)
            message += p.getMessage();
    }

    /**
     * Constructor for a received message, holds the packets until the message is completed
     *
     * @param telephoneNumber the telephone number
     * @param packet
     */
    SMSMessage(String telephoneNumber, SMSPacket packet) {

    }

    /**
     * Wrap for a text message, used to check the parameters validity
     *
     * @param telephoneNumber a valid telephone number to send the message to
     * @param messageText     a message
     * @throws InvalidSMSMessageException      if Utils.checkMessageText return false
     * @throws InvalidTelephoneNumberException if Utils.checkTelephoneNumber return false
     */
    public SMSMessage(String telephoneNumber, String messageText) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        //Checks on the telephone number
        SMSChecks.TelephoneNumberState telephoneNumberState = SMSChecks.checkTelephoneNumber(telephoneNumber);
        if (telephoneNumberState != SMSChecks.TelephoneNumberState.TELEPHONE_NUMBER_VALID) {
            throw new InvalidTelephoneNumberException("Telephone number not valid", telephoneNumberState);
        }
        this.telephoneNumber = telephoneNumber;
        //Checks on the message text
        SMSChecks.MessageTextState messageTextState = SMSChecks.checkMessageText(messageText);
        if (messageTextState != SMSChecks.MessageTextState.MESSAGE_TEXT_VALID)
            throw new InvalidSMSMessageException("text length exceeds maximum allowed", messageTextState);
        this.message = messageText;

        packets = SMSController.getPacketsFromMessage(this);
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
    public String getMessage() {
        return message;
    }

    public SMSPacket[] getPackets(){
        return packets;
    }

}
