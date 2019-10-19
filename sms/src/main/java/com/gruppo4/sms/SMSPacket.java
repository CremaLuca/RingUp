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


    public SMSPacket(int applicationCode, int messageCode, int packetNumber, int totalNumber, String message){
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

    public static SMSPacket createSMSPacket(String smsContent){
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
