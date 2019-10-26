package com.gruppo4.sms;

public class SMSPacket {

    static final int PACKAGE_MESSAGE_MAX_LENGTH = 144; //160 - 3(applicationCode) - 3(messageCode) - 3(packetNumber) - 3(totalNumber) - 4(SEPARATOR)
    private static final char SEPARATOR_CHAR = '_';
    static final String SEPARATOR = SEPARATOR_CHAR + ""; //Workaround because string + char concatenations has some problems
    private String message;
    private int applicationCode;
    private int messageCode;
    private int packetNumber;
    private int totalNumber;

    /**
     * @param applicationCode identifier for the app, must be a number between -99 and 999
     * @param messageCode     identifier for the message, must be a number between -99 and 999
     * @param packetNumber    packet progressive number, must be a number between 1 and totalNumber
     * @param totalNumber     number of packets for the whole message, must be greater than packetNumber and smaller than 999
     * @param message         the message to send, has to be shorter than PACKAGE_MESSAGE_MAX_LENGTH characters
     */
    SMSPacket(int applicationCode, int messageCode, int packetNumber, int totalNumber, String message) {
        if (applicationCode > 999 || applicationCode < -99)
            throw new IllegalArgumentException("Application code must be between -99 and +999");
        if (messageCode > 999 || messageCode < -99)
            throw new IllegalArgumentException("Message code must be between -99 and +999");
        if (packetNumber > 999 || packetNumber < 1)
            throw new IllegalArgumentException("Packet number must be between 1 and 999");
        if (totalNumber > 999 || totalNumber < 1)
            throw new IllegalArgumentException("Total number must be between 1 and 999");
        if (packetNumber > totalNumber)
            throw new IllegalStateException("Packet number must be no greater than total number");
        if (message.length() > PACKAGE_MESSAGE_MAX_LENGTH)
            throw new IllegalArgumentException("Message length must be shorter than " + PACKAGE_MESSAGE_MAX_LENGTH + " characters");

        this.message = message;
        this.applicationCode = applicationCode;
        this.messageCode = messageCode;
        this.packetNumber = packetNumber;
        this.totalNumber = totalNumber;
    }

    /**
     * Parses an sms to a packet
     *
     * @param smsContent the content of the SMS received
     * @return the packet
     */
    public static SMSPacket parseSMSPacket(String smsContent) {
        //Split the string in 4 groups divided by
        String[] splits = smsContent.split(SEPARATOR, 5);
        if (splits.length != 5)
            return null;

        try {
            int applicationCode = Integer.parseInt(splits[0]);
            int messageCode = Integer.parseInt(splits[1]);
            int packageNumber = Integer.parseInt(splits[2]);
            int totalNumber = Integer.parseInt(splits[3]);
            String message = splits[4];

            return new SMSPacket(applicationCode, messageCode, packageNumber, totalNumber, message);
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
        return applicationCode + SEPARATOR + messageCode + SEPARATOR + packetNumber + SEPARATOR + totalNumber + SEPARATOR + message;
    }

    int getTotalNumber() {
        return totalNumber;
    }

    int getPacketNumber() {
        return packetNumber;
    }

    int getMessageCode() {
        return messageCode;
    }

    int getApplicationCode() {
        return applicationCode;
    }

    String getMessage() {
        return message;
    }

}
