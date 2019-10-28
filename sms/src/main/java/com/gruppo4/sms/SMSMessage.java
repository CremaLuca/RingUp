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
    private int messageId;

    private SMSPacket[] packets;

    /**
     * Wrap for a text message, used to check the parameters validity
     *
     * @param telephoneNumber a valid telephone number to send the message to
     * @param packets     array of packets from which we construct the message
     */

    public SMSMessage(String telephoneNumber, SMSPacket[] packets)
    {
        this.telephoneNumber = telephoneNumber;
        this.messageId = packets[0].getMessageId();
        this.packets = packets;
        for (SMSPacket p: packets)
            message += p.getMessage();
    }

    /**
     * Constructor for a received message, holds the packets until the message is completed
     *
     * @param telephoneNumber the telephone number
     * @param packet the first packet received for this message, can be any packet of the message
     */
    SMSMessage(String telephoneNumber, SMSPacket packet) {
        this.telephoneNumber = telephoneNumber;
        this.messageId = packet.getMessageId();
        this.packets = new SMSPacket[packet.getTotalNumber()];
        packets[packet.getPacketNumber() - 1] = packet;
        if (isComplete()) {
            this.message = packet.getMessage();
            SMSController.callOnReceivedListeners(this);
        }
    }

    /**
     * Wrap for a text message, used to check the parameters validity
     *
     * @param telephoneNumber a valid telephone number to send the message to
     * @param messageText     a message
     * @throws InvalidSMSMessageException      if Utils.checkMessageText returns false
     * @throws InvalidTelephoneNumberException if Utils.checkTelephoneNumber returns false
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
     * Adds a packet to this message
     *
     * @param packet
     */
    public void addPacket(SMSPacket packet) {
        packets[packet.getPacketNumber() - 1] = packet;
        if (isComplete()) {
            //Generate the message
            this.message = getMessageFromPackets();
            SMSController.callOnReceivedListeners(this);
        }
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

    public int getMessageId() {
        return messageId;
    }

    /**
     * Checks if the message has all the packets
     *
     * @return true if there are no missing packets, false otherwise
     */
    private boolean isComplete() {
        for (SMSPacket packet : packets) {
            if (packet == null)
                return false;
        }
        return true;
    }

    /**
     * MUST BE CALLED ONLY IF isComplete IS TRUE
     *
     * @return
     */
    private String getMessageFromPackets() {
        String message = "";
        for (SMSPacket packet : packets) {
            message += packet.getMessage();
        }
        return message;
    }

}
