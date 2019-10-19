package com.gruppo4.sms;

class SMSPacket {

    private String message;
    private int applicationCode;
    private int messageCode;
    private int packetNumber;
    private int totalNumber;

    static final char SEPARATOR = '-';


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

    public static SMSPacket createSMSPacket(String smsContent){
        //Split the string in 4 groups divided by
        String[] splits = smsContent.split(SEPARATOR + "",5); // SEPARATOR + "" because this needs a String not a char
        return new SMSPacket(Integer.getInteger(splits[0]),Integer.getInteger(splits[1]),Integer.getInteger(splits[2]),Integer.getInteger(splits[3]),splits[4]);
    }

}
