package com.gruppo4.sms;

import android.util.Log;

import com.gruppo4.sms.utils.SMSChecks;

public class SMSPacket {

    public static final int MAX_PACKET_TEXT_LEN = 144; //160 - 3(applicationCode) - 3(messageId) - 3(packetNumber) - 3(totalNumber) - 4(SEPARATOR)
    public static final String SEPARATOR = "_";
    private String message;
    private int applicationCode;
    private int messageId;
    private int packetNumber;
    private int totalNumber;

    /**
     * @param applicationCode identifier for the app, must be >= -99 and <= 999
     * @param messageId       identifier for the message, must be >= -99 and <= 999
     * @param packetNumber    packet progressive number, must be a number between 1 and totalNumber
     * @param totalNumber     number of packets for the whole message, must <= 999
     * @param packetMsgText         the message to send, has to be shorter than PACKET_MESSAGE_MAX_LENGTH characters
     */
    public SMSPacket(int applicationCode, int messageId, int packetNumber, int totalNumber, String packetMsgText) {
        if (applicationCode > 999 || applicationCode < -99)
            throw new IllegalArgumentException("Application code must be between -99 and +999");
        if (messageId > 999 || messageId < -99)
            throw new IllegalArgumentException("Message id must be between -99 and +999");
        if (packetNumber > 999 || packetNumber < 1)
            throw new IllegalArgumentException("Packet number must be between 1 and 999");
        if (totalNumber > 999 || totalNumber < 1)
            throw new IllegalArgumentException("Total number must be between 1 and 999");
        if (packetNumber > totalNumber)
            throw new IllegalStateException("Packet number must be no greater than total number");
        if (packetMsgText.length() > MAX_PACKET_TEXT_LEN )
            throw new IllegalArgumentException("Message length must be shorter than " + MAX_PACKET_TEXT_LEN  + " characters");

        message = packetMsgText;
        this.applicationCode = applicationCode;
        this.messageId = messageId;
        this.packetNumber = packetNumber;
        this.totalNumber = totalNumber;
    }

    /**
     * Separates the various parts of the packet with SEPARATORs and returns the chained string
     *
     * @return A String that can be sent in a SMS
     */
    public String getText() {
        return applicationCode + SEPARATOR + messageId +
                SEPARATOR + packetNumber + SEPARATOR + totalNumber + SEPARATOR + message;
    }

    public int getTotalNumber() {
        return totalNumber;
    }
    public int getPacketNumber() {
        return packetNumber;
    }
    public int getMessageId() { return messageId; }

    public int getApplicationCode() {
        return applicationCode;
    }
    public String getMessage() {
        return message;
    }
}
