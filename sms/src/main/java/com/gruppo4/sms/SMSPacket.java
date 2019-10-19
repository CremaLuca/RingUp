package com.gruppo4.sms;

import android.util.Log;

class SMSPacket {

    private String message;
    private int applicationCode;
    private int messageCode;
    private int packetNumber;
    private int totalNumber;

    static final char SEPARATOR = '-';
    public static final int PACKAGE_MESSAGE_MAX_LENGTH = 142; //160 - 4(applicationCode) - 4(messageCode) - 3(packetNumber) - 3(totalNumber) - 4(SEPARATOR)


    public SMSPacket(int applicationCode, int messageCode, int packetNumber, int totalNumber, String message){
        this.message = message;
        this.applicationCode = applicationCode;
        this.messageCode = messageCode;
        this.packetNumber = packetNumber;
        this.totalNumber = totalNumber;
    }

    public String getSMSOutput(){
        Log.d("SMSPacket",SEPARATOR + " <- separator");
        String separator = SEPARATOR + "";
        return  applicationCode + separator + messageCode + separator + packetNumber +  separator + totalNumber + separator + message;
    }

    public static SMSPacket createSMSPacket(String smsContent){
        //Split the string in 4 groups divided by
        String[] splits = smsContent.split(SEPARATOR + "",5); // SEPARATOR + "" because this needs a String not a char
        return new SMSPacket(Integer.parseInt(splits[0]),Integer.parseInt(splits[1]),Integer.parseInt(splits[2]),Integer.parseInt(splits[3]),splits[4]);
    }

}
