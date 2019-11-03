package com.gruppo4.sms.dataLink;

import android.content.Context;

import com.gruppo4.communication.Message;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;

import java.util.ArrayList;

public class SMSMessage implements Message<String, SMSPeer> {

    public static final int MAX_ID = 999;

    //This is because package number cannot exceed three characters
    static final int MAX_PACKETS = 999;
    public static final int MAX_MSG_TEXT_LEN = SMSPacket.MAX_PACKET_TEXT_LEN * MAX_PACKETS; //we deliver at most 999 packets
    private SMSPeer peer;
    private StringBuilder message;
    private int messageId;
    private SMSPacket[] packets;

    /**
     * Wrap for a text message, used to check the parameters validity
     *
     * @param destination a valid destination peer
     * @param packets     array of packets from which we construct the message
     */

    SMSMessage(SMSPeer destination, SMSPacket[] packets) {
        this.peer = destination;
        this.messageId = packets[0].getMessageId();
        this.packets = packets;
        for (SMSPacket p : packets)
            message.append(p.getMessageText());
    }

    /**
     * Constructor for a received message, holds the packets until the message is completed
     *
     * @param destination the destination peer
     */
    SMSMessage(SMSPeer destination, SMSPacket packet) {
        this.peer = destination;
        addPacket(packet);
    }

    /**
     * Wrap for a text message, used to check the parameters validity
     *
     * @param ctx         the current application/service context
     * @param destination a valid peer
     * @param messageText a message
     * @throws InvalidSMSMessageException      if Utils.checkMessageText returns false
     * @throws InvalidTelephoneNumberException if Utils.checkTelephoneNumber returns false
     */
    public SMSMessage(Context ctx, SMSPeer destination, String messageText) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        //Checks on the telephone number
        SMSPeer.TelephoneNumberState telephoneNumberState = destination.checkPhoneNumber();
        if (telephoneNumberState != SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_VALID) {
            throw new InvalidTelephoneNumberException("Telephone number not valid", telephoneNumberState);
        }
        this.peer = destination;
        //Checks on the message text
        MessageTextState messageTextState = checkMessageText(messageText);
        if (messageTextState != MessageTextState.MESSAGE_TEXT_VALID)
            throw new InvalidSMSMessageException("text length exceeds maximum allowed", messageTextState);

        this.messageId = SMSHandler.getNewMessageId(ctx); //Sequential code
        this.message = new StringBuilder(messageText);
        packets = getPacketsFromText(ctx, messageText);
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


    /**
     * Adds a packet to this message
     *
     * @param packet add packet to packets list
     */
    void addPacket(SMSPacket packet) {
        if (packets == null) {
            packets = new SMSPacket[packet.getTotalNumber()];
            messageId = packet.getMessageId();
        } else if (packets[packet.getPacketNumber() - 1] != null) {
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
    @Override
    public SMSPeer getPeer() {
        return peer;
    }

    private void constructMessage() {
        message = new StringBuilder();
        for (SMSPacket packet : packets)
            message.append(packet.getMessageText());
    }

    /**
     * @return the message
     */
    @Override
    public String getData() {
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
    ArrayList<String> getPacketsContent() {
        ArrayList<String> content = new ArrayList<>();
        for (int i = 0; i < packets.length; i++) {
            content.add(packets[i].getSMSData());
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
        return true;
    }

    private SMSPacket[] getPacketsFromText(Context ctx, String messageText) {
        int rem = messageText.length() % SMSPacket.MAX_PACKET_TEXT_LEN; //This is last message length
        int packetsCount = messageText.length() / SMSPacket.MAX_PACKET_TEXT_LEN + (rem != 0 ? 1 : 0);
        SMSPacket[] packets = new SMSPacket[packetsCount];
        String subText;
        for (int i = 0; i < packetsCount; i++) {
            int finalCharacter = Math.min((i + 1) * SMSPacket.MAX_PACKET_TEXT_LEN, messageText.length());
            subText = messageText.substring(i * SMSPacket.MAX_PACKET_TEXT_LEN, finalCharacter);
            packets[i] = new SMSPacket(SMSHandler.getApplicationCode(ctx), messageId, i + 1, packetsCount, subText);
        }
        return packets;
    }

    public enum MessageTextState {
        MESSAGE_TEXT_VALID,
        MESSAGE_TEXT_TOO_LONG
    }
}
