package com.gruppo4.sms;

import android.util.Log;

import com.gruppo4.sms.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.utils.SMSChecks;

public class SMSMessage {

    //This is because package number cannot exceed three characters
    static final int MAX_PACKETS = 999;
    public static final int MAX_MSG_TEXT_LEN = SMSPacket.MAX_PACKET_TEXT_LEN * MAX_PACKETS; //we deliver at most 999 packets
    private String telephoneNumber;
    private StringBuilder message;
    private int messageId;

    private SMSPacket[] packets;

    /**
     * Wrap for a text message, used to check the parameters validity
     *
     * @param telephoneNumber a valid telephone number to send the message to
     * @param packets         array of packets from which we construct the message
     */

    SMSMessage(String telephoneNumber, SMSPacket[] packets) {
        this.telephoneNumber = telephoneNumber;
        this.messageId = packets[0].getMessageId();
        this.packets = packets;
        for (SMSPacket p : packets)
            message.append(p.getMessageText());
    }

    /**
     * Constructor for a received message, holds the packets until the message is completed
     *
     * @param telephoneNumber the telephone number
     */
    SMSMessage(String telephoneNumber, SMSPacket packet) {
        this.telephoneNumber = telephoneNumber;
        addPacket(packet);
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

        this.messageId = SMSController.getNewMessageId(); //Sequential code
        this.message = new StringBuilder(messageText);
        packets = getPacketsFromText(messageText);
    }

    /**
     * Adds a packet to this message
     *
     * @param packet add packet to packets list
     */
    void addPacket(SMSPacket packet) {
        if (packets == null) {
            Log.v("SMSMessage", "Creating a new array for an incoming packet");
            packets = new SMSPacket[packet.getTotalNumber()];
            messageId = packet.getMessageId();
        } else if (packets[packet.getPacketNumber() - 1] != null) {
            Log.v("SMSMessage", "This message already has another packet in that position, could mean that the sender sent the same code twice?");
            throw new IllegalStateException("There shouldn't be a packet for this message");
        }
        packets[packet.getPacketNumber() - 1] = packet;
        if (hasAllPackets()) constructMessage();
    }

    /**
     * telephoneNumber is either the number from which the message comes from, or the number where to send the message
     *
     * @return the telephone number
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    private void constructMessage() {
        message = new StringBuilder();
        for (SMSPacket packet : packets)
            message.append(packet.getMessageText());
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message.toString();
    }

    /**
     * @return a list of packets
     */
    SMSPacket[] getPackets() {
        return packets;
    }

    /**
     * Creates an array of strings that contain the SMSData for each packet
     *
     * @return an array of Strings containing SMSData for each packet
     */
    String[] getPacketsContent() {
        String[] content = new String[packets.length];
        for (int i = 0; i < packets.length; i++) {
            content[i] = packets[i].getSMSData();
        }
        return content;
    }

    /**
     * Returns a unique sequential code for this message
     *
     * @return the message id
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * Checks if the message has all the packets
     *
     * @return true if there are no missing packets, false otherwise
     */
    boolean hasAllPackets() {
        for (SMSPacket packet : packets) {
            if (packet == null)
                return false;
        }
        Log.v("SMSMessage", "Message " + messageId + " is now complete");
        return true;
    }

    private SMSPacket[] getPacketsFromText(String messageText) {
        int rem = messageText.length() % SMSPacket.MAX_PACKET_TEXT_LEN; //This is last message length
        int packetsCount = messageText.length() / SMSPacket.MAX_PACKET_TEXT_LEN + (rem != 0 ? 1 : 0);
        Log.v("SMSMessage", "We've got to send " + packetsCount + " messages for a message " + messageText.length() + " characters long");
        SMSPacket[] packets = new SMSPacket[packetsCount];
        String subText;
        for (int i = 0; i < packetsCount; i++) {
            int finalCharacter = Math.min((i + 1) * SMSPacket.MAX_PACKET_TEXT_LEN, messageText.length());
            Log.v("SMSMessage", "Substring from " + i * SMSPacket.MAX_PACKET_TEXT_LEN + " to " + finalCharacter + " for packet number: " + (i + 1));
            subText = messageText.substring(i * SMSPacket.MAX_PACKET_TEXT_LEN, finalCharacter);
            packets[i] = new SMSPacket(SMSController.getApplicationCode(), messageId, i + 1, packetsCount, subText);
        }
        return packets;
    }

}
