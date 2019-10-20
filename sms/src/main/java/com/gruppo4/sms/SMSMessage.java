package com.gruppo4.sms;

public class SMSMessage {

    private String msgBody;
    private String destinationAddress;

    private static final int MAX_MSGBODY_LENGTH = 160;
    private static final int MAX_DESTINATIONADDRESS_LENGTH = 15;
    private static final int MIN_DESTINATIONADDRESS_LENGTH = 5;

    public SMSMessage(String msgBody, String destinationAddress) throws MsgBodyException, DestinationAddressException{
        this.msgBody = checkMsgBody(msgBody);
        this.destinationAddress = checkDestinationAddress(destinationAddress);
    }

    public String getMsgBody() {
        return msgBody;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    private String checkMsgBody(String msgBody) throws MsgBodyException {
        if(msgBody.length() > MAX_MSGBODY_LENGTH)
            throw new MsgBodyException("message body too long (" + msgBody.length() + ")");
        return msgBody;
    }

    private String checkDestinationAddress(String destinationAddress) throws DestinationAddressException {
        if(destinationAddress.length() > MAX_DESTINATIONADDRESS_LENGTH || destinationAddress.length() < MIN_DESTINATIONADDRESS_LENGTH)
            throw new DestinationAddressException("destination address invalid length (" + destinationAddress.length() + ")");
        return destinationAddress;
    }
}
