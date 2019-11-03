package com.gruppo4.sms.dataLink;

/**
 * Represents a single SMS as a part of a bigger message
 */
class SMSPacket {

    static final int MAX_PACKET_TEXT_LEN = 140; //160 - 3(applicationCode) - 3(messageId) - 3(packetNumber) - 3(totalNumber) - 4(SEPARATOR)
    public static final String SEPARATOR = "_";
    private String messageText;
    private int applicationCode;
    private int messageId;
    private int packetNumber;
    private int totalNumber;

    /**
     * Constructor for the packet, checks the data
     * @param applicationCode identifier for the app
     * @param messageId       identifier for the messageText
     * @param packetNumber    packet progressive number
     * @param totalNumber     number of packets for the whole messageText
     * @param packetMsgText   the messageText to send
     */
    SMSPacket(int applicationCode, int messageId, int packetNumber, int totalNumber, String packetMsgText) {
        if (applicationCode > 999 || applicationCode < 0)
            throw new IllegalArgumentException("Application code must be between 0 and 999");
        if (messageId > 999 || messageId < 0)
            throw new IllegalArgumentException("Message id must be between 0 and 999");
        if (packetNumber > 999 || packetNumber < 1)
            throw new IllegalArgumentException("Packet number must be between 1 and 999");
        if (totalNumber > 999 || totalNumber < 1)
            throw new IllegalArgumentException("Total number must be between 1 and 999");
        if (packetNumber > totalNumber)
            throw new IllegalStateException("Packet number must be no greater than total number");
        if (packetMsgText.length() > MAX_PACKET_TEXT_LEN)
            throw new IllegalArgumentException("Message length must be shorter than " + MAX_PACKET_TEXT_LEN + " characters");

        messageText = packetMsgText;
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
    String getSMSData() {
        return applicationCode + SEPARATOR + messageId +
                SEPARATOR + packetNumber + SEPARATOR + totalNumber + SEPARATOR + messageText;
    }

    /**
     * The total number of packets that compose the message
     *
     * @return the number of packets for the message
     */
    int getTotalNumber() {
        return totalNumber;
    }

    int getPacketNumber() {
        return packetNumber;
    }

    int getMessageId() {
        return messageId;
    }

    int getApplicationCode() {
        return applicationCode;
    }

    String getMessageText() {
        return messageText;
    }
}
