package com.gruppo4.sms;

import android.util.Log;

class SMSPacket {

    private String message;
    private int applicationCode;
    private int messageCode;
    private int packetNumber;
    private int totalNumber;

    static final char SEPARATOR_CHAR = '-';
    static final String SEPARATOR = SEPARATOR_CHAR + ""; //Workaround because string + char concatenations gives some problems

    public static final int PACKAGE_MESSAGE_MAX_LENGTH = 142; //160 - 4(applicationCode) - 4(messageCode) - 3(packetNumber) - 3(totalNumber) - 4(SEPARATOR)


    /**
     *
     * @param applicationCode identifier for the app, must be a number between -99 and 999
     * @param messageCode identifier for the message, must be a number between -99 and 999
     * @param packetNumber packet progressive number, must be a number between 1 and totalNumber
     * @param totalNumber number of packets for the whole message, must be greater than packetNumber and smaller than 999
     * @param message the message to send, has to be shorter than PACKAGE_MESSAGE_MAX_LENGTH characters
     */
    public SMSPacket(int applicationCode, int messageCode, int packetNumber, int totalNumber, String message){
        if(applicationCode > 999 || applicationCode < -99)
            throw new IllegalArgumentException("Application code must be between -99 and +999");
        if(messageCode > 999 || messageCode < -99)
            throw new IllegalArgumentException("Message code must be between -99 and +999");
        if(packetNumber > 999 || packetNumber < 1)
            throw new IllegalArgumentException("Packet number must be between 1 and 999");
        if(totalNumber > 999 || totalNumber < 1)
            throw new IllegalArgumentException("Total number must be between 1 and 999");
        if(packetNumber > totalNumber)
            throw new IllegalStateException("Packet number must be no greater than total number");
        if(message.length() > PACKAGE_MESSAGE_MAX_LENGTH)
            throw new IllegalArgumentException("Message length must be shorter than " + PACKAGE_MESSAGE_MAX_LENGTH + " characters");

        this.message = message;
        this.applicationCode = applicationCode;
        this.messageCode = messageCode;
        this.packetNumber = packetNumber;
        this.totalNumber = totalNumber;
    }

    public String getSMSOutput(){
        return  applicationCode + SEPARATOR + messageCode + SEPARATOR + packetNumber +  SEPARATOR + totalNumber + SEPARATOR + message;
    }

    public int getTotalNumber(){
        return totalNumber;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public int getMessageCode() {
        return messageCode;
    }

    public int getApplicationCode() {
        return applicationCode;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Parses an sms to a packet
     * @param smsContent
     * @return the packet
     */
    public static SMSPacket parseSMSPacket(String smsContent){
        //Split the string in 4 groups divided by
        String[] splits = smsContent.split(SEPARATOR,5);
        if(splits.length != 5)
            return null;

        try {
            int applicationCode = Integer.parseInt(splits[0]);
            int messageCode = Integer.parseInt(splits[1]);
            int packageNumber = Integer.parseInt(splits[2]);
            int totalNumber = Integer.parseInt(splits[3]);
            String message = splits[4];

            return new SMSPacket(applicationCode, messageCode, packageNumber, totalNumber, message);
        }catch (NumberFormatException e){
            //If by chance the message has 4 separators, then we see if there are actually numbers
            return null;
        }
    }

}
