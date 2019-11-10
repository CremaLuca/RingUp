package com.gruppo4.sms;

/**
 * @author Tommasini Marco
 */
public class SMSMessage {

    private String msgBody;
    private String destinationAddress;

    private static final int MAX_MSGBODY_LENGTH = 160;
    private static final int MAX_DESTINATIONADDRESS_LENGTH = 15;
    private static final int MIN_DESTINATIONADDRESS_LENGTH = 5;

    /**
     * Creates the message and handles exceptions during creation
     * @param msgBody
     * @param destinationAddress
     */
    public SMSMessage(String msgBody, String destinationAddress) {
        this.msgBody = checkMsgBody(msgBody);
        this.destinationAddress = checkDestinationAddress(destinationAddress);
    }

    public String getMsgBody() {
        return msgBody;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * Checks if the Body length of the message is correct
     * @param msgBody
     * @return
     */
    private String checkMsgBody(String msgBody) {
        if(msgBody.length() > MAX_MSGBODY_LENGTH)
            return null;
        return msgBody;
    }

    /**
     * Checks if the Address length of the message is correct
     * @param destinationAddress
     * @return
     */
    private String checkDestinationAddress(String destinationAddress) {
        if(destinationAddress.length() > MAX_DESTINATIONADDRESS_LENGTH || destinationAddress.length() < MIN_DESTINATIONADDRESS_LENGTH)
            return null;
        return destinationAddress;
    }
}
