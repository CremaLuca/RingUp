package com.gruppo4.sms;

import android.util.Log;

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
        try {
            this.msgBody = checkMsgBody(msgBody);
            this.destinationAddress = checkDestinationAddress(destinationAddress);
        } catch (MsgBodyException e) {
            Log.e("SMSMessage", e.getMessage());
        } catch (DestinationAddressException e) {
            Log.e("SMSMessage", e.getMessage());
        }
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
     * @throws MsgBodyException
     */
    private String checkMsgBody(String msgBody) throws MsgBodyException {
        if(msgBody.length() > MAX_MSGBODY_LENGTH)
            throw new MsgBodyException("message body too long (" + msgBody.length() + ")");
        return msgBody;
    }

    /**
     * Checks if the Address length of the message is correct
     * @param destinationAddress
     * @return
     * @throws DestinationAddressException
     */
    private String checkDestinationAddress(String destinationAddress) throws DestinationAddressException {
        if(destinationAddress.length() > MAX_DESTINATIONADDRESS_LENGTH || destinationAddress.length() < MIN_DESTINATIONADDRESS_LENGTH)
            throw new DestinationAddressException("destination address invalid length (" + destinationAddress.length() + ")");
        return destinationAddress;
    }
}
