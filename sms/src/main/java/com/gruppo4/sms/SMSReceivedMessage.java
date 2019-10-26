package com.gruppo4.sms;

public class SMSReceivedMessage {

    private SMSPacket[] packets;

    private int applicationCode;
    private int messageCode;
    private String telephoneNumber;

    /**
     * @param packet
     * @param telephoneNumber
     */
    SMSReceivedMessage(SMSPacket packet, String telephoneNumber) {
        packets = new SMSPacket[packet.getTotalNumber()];
        packets[packet.getPacketNumber() - 1] = packet;

        this.applicationCode = packet.getApplicationCode();
        this.messageCode = packet.getMessageCode();
        this.telephoneNumber = telephoneNumber;
        //If we have all the packets for this message (1 in this case) we can notify the app that we received a message
        if (checkCompleted())
            SMSController.callOnReceivedListeners(this);
    }

    /**
     * @param packet
     */
    void addPacket(SMSPacket packet) {
        if (packets[packet.getPacketNumber() - 1] != null)
            throw new IllegalStateException("There shouldn't be another packet at the position " + packet.getPacketNumber() + " for the message " + messageCode);

        packets[packet.getPacketNumber() - 1] = packet;
        //If we have all the packets we call the listeners
        if (checkCompleted())
            SMSController.callOnReceivedListeners(this);
    }

    /**
     * @return
     */
    public int getApplicationCode() {
        return applicationCode;
    }

    /**
     *
     * @return
     */
    int getMessageCode() {
        return messageCode;
    }

    /**
     *
     * @return
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * This method can only be called once the message is fully re-constructed
     * @return the message content
     */
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        for (SMSPacket packet : packets) {
            message.append(packet.getMessage());
        }
        return message.toString();
    }

    /**
     * Controls if the message is fully re-constructed by its packets
     * @return
     */
    private boolean checkCompleted() {
        for (SMSPacket packet : packets) {
            if (packet == null)
                return false;
        }
        return true;
    }

}
