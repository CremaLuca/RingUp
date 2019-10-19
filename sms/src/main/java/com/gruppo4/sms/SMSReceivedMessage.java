package com.gruppo4.sms;

public class SMSReceivedMessage {

    private SMSPacket[] packets;

    private int applicationCode;
    private int messageCode;
    private String telephoneNumber;

    public SMSReceivedMessage(SMSPacket packet, String telephoneNumber){
        packets = new SMSPacket[packet.getTotalNumber()];
        packets[packet.getPacketNumber() - 1] = packet;

        this.applicationCode = packet.getApplicationCode();
        this.messageCode = packet.getMessageCode();
        this.telephoneNumber = telephoneNumber;

        if(checkCompleted())
            SMSController.callReceiveListeners(this);
    }

    public void addPacket(SMSPacket packet){
        packets[packet.getPacketNumber() - 1] = packet;
        if(checkCompleted())
            SMSController.callReceiveListeners(this);
    }

    public int getApplicationCode() {
        return applicationCode;
    }

    public int getMessageCode() {
        return messageCode;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getMessage(){
        String message = "";
        for(SMSPacket packet : packets){
            message += packet.getMessage();
        }
        return message;
    }

    protected boolean checkCompleted(){
        for (int i=0;i<packets.length;i++){
            if(packets[i] == null)
                return false;
        }
        return true;
    }

}
