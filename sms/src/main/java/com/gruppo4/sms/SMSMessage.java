package com.gruppo4.sms;

import android.util.Log;

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
    public SMSMessage(String telephoneNumber, SMSPacket packet) {
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

        packets = getPacketsFromMessage(this);
    }

    /**
     * Adds a packet to this message
     *
     * @param packet add packet to packets list
     */
    public void addPacket(SMSPacket packet) {
        packets[packet.getPacketNumber() - 1] = packet;
        if (isComplete()) {
            //Generate the message
            this.message = getMessageFromPackets();
            Log.d("DEBUG/SMSMessage", "message is completed");
            SMSController.callOnReceivedListeners(this);
        }
    }

    /**
     * telephoneNumber is either the number from which the message comes from, or the number where to send the message
     *
     * @return the telephone number
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

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
     * @return the complete message
     */
    public String getMessageFromPackets() {
        String message = "";
        for (SMSPacket packet : packets) {
            message += packet.getMessage();
        }
        return message;
    }

    public  SMSPacket[] getPacketsFromMessage(SMSMessage message){
        String msgText = message.getMessage();
        int rem = msgText.length() % SMSPacket.MAX_PACKET_TEXT_LEN;
        int packetsCount = msgText.length() / SMSPacket.MAX_PACKET_TEXT_LEN + (rem != 0 ? 1:0);
        Log.d("DEBUG/SMSMessage", "I have to send " + packetsCount + " messages for a message " + msgText.length() + " characters long");
        SMSPacket[] packets = new SMSPacket[packetsCount];
        int msgId = SMSController.getNewMessageId();
        String subText;
        for (int i = 0; i < packetsCount; i++) {
            int finalCharacter = Math.min((i + 1) * SMSPacket.MAX_PACKET_TEXT_LEN, msgText.length());
            Log.d("DEBUG/SMSMessage", "Substring from " + i *  SMSPacket.MAX_PACKET_TEXT_LEN + " to " + finalCharacter);
            subText =msgText.substring(i * SMSPacket.MAX_PACKET_TEXT_LEN, finalCharacter);
            packets[i] = new SMSPacket(SMSController.getApplicationCode(), msgId, i + 1, packetsCount, subText);
        }
        return packets;
    }

}
