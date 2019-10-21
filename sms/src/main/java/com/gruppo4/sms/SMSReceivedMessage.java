package com.gruppo4.sms;

public class SMSReceivedMessage {

    private SMSPacket[] packets;

    private int applicationCode;
    private int messageCode;
    private String telephoneNumber;

    SMSReceivedMessage(SMSPacket packet, String telephoneNumber) {
        packets = new SMSPacket[packet.getTotalNumber()];
        packets[packet.getPacketNumber() - 1] = packet;

        this.applicationCode = packet.getApplicationCode();
        this.messageCode = packet.getMessageCode();
        this.telephoneNumber = telephoneNumber;

        if (checkCompleted())
            SMSController.callReceiveListeners(this);
    }

    void addPacket(SMSPacket packet) {
        packets[packet.getPacketNumber() - 1] = packet;
        if (checkCompleted())
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

    public String getMessage() {
        StringBuilder message = new StringBuilder();
        for (SMSPacket packet : packets) {
            message.append(packet.getMessage());
        }
        return message.toString();
    }

    private boolean checkCompleted() {
        for (SMSPacket packet : packets) {
            if (packet == null)
                return false;
        }
        return true;
    }

}
