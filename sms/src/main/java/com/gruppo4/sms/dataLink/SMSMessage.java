package com.gruppo4.sms.dataLink;

import android.content.Context;

import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;

import java.util.ArrayList;

public class SMSMessage {

    public static final int MAX_ID = 999;
    public static final int MAX_TELEPHONE_NUMBER_LENGTH = 20;
    public static final int MIN_TELEPHONE_NUMBER_LENGTH = 7;
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
     * @param ctx the current application/service context
     * @param telephoneNumber a valid telephone number to send the message to
     * @param messageText     a message
     * @throws InvalidSMSMessageException      if Utils.checkMessageText returns false
     * @throws InvalidTelephoneNumberException if Utils.checkTelephoneNumber returns false
     */
    public SMSMessage(Context ctx, String telephoneNumber, String messageText) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        //Checks on the telephone number
        TelephoneNumberState telephoneNumberState = checkTelephoneNumber(telephoneNumber);
        if (telephoneNumberState != TelephoneNumberState.TELEPHONE_NUMBER_VALID) {
            throw new InvalidTelephoneNumberException("Telephone number not valid", telephoneNumberState);
        }
        this.telephoneNumber = telephoneNumber;
        //Checks on the message text
        MessageTextState messageTextState = checkMessageText(messageText);
        if (messageTextState != MessageTextState.MESSAGE_TEXT_VALID)
            throw new InvalidSMSMessageException("text length exceeds maximum allowed", messageTextState);

        this.messageId = SMSController.getNewMessageId(ctx); //Sequential code
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
     * Checks if the phone number is valid
     *
     * @param telephoneNumber the phone number to check
     * @return The state of the telephone number after the tests
     */
    public static TelephoneNumberState checkTelephoneNumber(String telephoneNumber) {
        //Check if the number is shorter than the MAX.
        if (telephoneNumber.length() > MAX_TELEPHONE_NUMBER_LENGTH) {
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_LONG;
        }
        //Check if the number is longer than the MIN.
        if (telephoneNumber.length() < MIN_TELEPHONE_NUMBER_LENGTH) {
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_SHORT;
        }
        //Check if it's actually a number and doesn't contain anything else
        //First we have to remove the "+"
        if (!telephoneNumber.substring(1, telephoneNumber.length() - 1).matches("[0-9]+")) {
            return TelephoneNumberState.TELEPHONE_NUMBER_NOT_A_NUMBER;
        }
        //Check if there is a country code.
        if (telephoneNumber.charAt(0) != '+') {
            return TelephoneNumberState.TELEPHONE_NUMBER_NO_COUNTRY_CODE;
        }
        //If it passed all the tests we are sure the number is valid.
        return TelephoneNumberState.TELEPHONE_NUMBER_VALID;
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
            packets[i] = new SMSPacket(SMSController.getApplicationCode(ctx), messageId, i + 1, packetsCount, subText);
        }
        return packets;
    }

    public enum TelephoneNumberState {
        TELEPHONE_NUMBER_VALID,
        TELEPHONE_NUMBER_TOO_SHORT,
        TELEPHONE_NUMBER_TOO_LONG,
        TELEPHONE_NUMBER_NO_COUNTRY_CODE,
        TELEPHONE_NUMBER_NOT_A_NUMBER
    }

    public enum MessageTextState {
        MESSAGE_TEXT_VALID,
        MESSAGE_TEXT_TOO_LONG
    }
}
