package com.gruppo4.sms;

import android.util.Log;

import com.gruppo4.sms.utils.SMSChecks;

public class SMSPacket {

    static final int MAX_PACKET_TEXT_LEN = 141; //160 - 3(applicationCode) - 2(messageCode) - 3(messageId) - 3(packetNumber) - 3(totalNumber) - 5(SEPARATOR)
    static final String SEPARATOR = "_";
    private String text;
    private int applicationCode;
    private int messageCode;
    private int messageId;
    private int packetNumber;
    private int totalNumber;

    /**
     * @param applicationCode identifier for the app, must be >= -99 and <= 999
     * @param messageId       identifier for the message, must be >= -99 and <= 999
     * @param messageCode     code for the message, must be >= -9 and <= 99
     * @param packetNumber    packet progressive number, must be a number between 1 and totalNumber
     * @param totalNumber     number of packets for the whole message, must <= 999
     * @param packetText         the message to send, has to be shorter than PACKET_MESSAGE_MAX_LENGTH characters
     */
    SMSPacket(int applicationCode,  int messageCode, int messageId, int packetNumber, int totalNumber, String packetText) {
        if (applicationCode > 999 || applicationCode < -99)
            throw new IllegalArgumentException("Application code must be between -99 and +999");
        if (messageId > 999 || messageId < -99)
            throw new IllegalArgumentException("Message id must be between -99 and +999");
        if (messageCode > 999 || messageCode < -99)
            throw new IllegalArgumentException("Message code must be between -99 and +999");
        if (packetNumber > 999 || packetNumber < 1)
            throw new IllegalArgumentException("Packet number must be between 1 and 999");
        if (totalNumber > 999 || totalNumber < 1)
            throw new IllegalArgumentException("Total number must be between 1 and 999");
        if (packetNumber > totalNumber)
            throw new IllegalStateException("Packet number must be no greater than total number");
        if (packetText.length() > MAX_PACKET_TEXT_LEN )
            throw new IllegalArgumentException("Message length must be shorter than " + MAX_PACKET_TEXT_LEN  + " characters");

        text = packetText;
        this.applicationCode = applicationCode;
        this.messageCode = messageCode;
        this.messageId = messageId;
        this.packetNumber = packetNumber;
        this.totalNumber = totalNumber;
    }

    /**
     * Parses an sms to a packet
     * @param smsContent the content of the SMS received
     * @return the packet
     */
    public static SMSPacket parseSMSPacket(String smsContent) {
        //Split the string in 4 groups divided by
        String[] splits = smsContent.split(SEPARATOR, 6);
        if (splits.length != 6)
            return null;

        try {
            int applicationCode = Integer.parseInt(splits[0]);
            int messageCode = Integer.parseInt(splits[1]);
            int messageId = Integer.parseInt(splits[2]);
            int packetNumber = Integer.parseInt(splits[3]);
            int totalNumber = Integer.parseInt(splits[4]);
            String text = splits[5];

            return new SMSPacket(applicationCode, messageCode, messageId, packetNumber, totalNumber, text);
        } catch (NumberFormatException e) {
            //If by chance the message has 4 separators, then we check if there are actually numbers
            return null;
        }
    }

    /**
     * Separates the various parts of the packet with SEPARATORs and returns the chained string
     *
     * @return A String that can be sent in a SMS
     */
    String getSMSOutput() {
        return applicationCode + SEPARATOR + messageCode + SEPARATOR + messageId +
                SEPARATOR + packetNumber + SEPARATOR + totalNumber + SEPARATOR + text;
    }

    int getTotalNumber() {
        return totalNumber;
    }

    int getPacketNumber() {
        return packetNumber;
    }

    int getMessageCode() { return messageCode; }

    int getMessageId() { return messageId; }

    int getApplicationCode() {
        return applicationCode;
    }

    String getText() {
        return text;
    }
}
