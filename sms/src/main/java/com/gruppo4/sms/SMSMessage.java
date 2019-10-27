package com.gruppo4.sms;

import android.util.Log;
import com.gruppo4.sms.utils.SMSChecks;
import com.gruppo4.sms.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.exceptions.InvalidTelephoneNumberException;
import java.util.ArrayList;

public class SMSMessage {

    public enum MessageTextState {
        MESSAGE_TEXT_VALID,
        MESSAGE_TEXT_TOO_LONG
    }

    //This is because package number cannot exceed three characters
    public static  final  int MAX_PACKETS = 999;
    public static final int MAX_MSG_TEXT_LEN = SMSPacket.MAX_PACKET_TEXT_LEN * MAX_PACKETS; //we deliver at most 999 packets
    private String telephoneNumber;
    private String message;

    private SMSPacket[] packets;

    /**
     * Wrap for a text message, used to check the parameters validity
     *
     * @param telephoneNumber a valid telephone number to send the message to
     * @param packets     array of packets from which we construct the message
     * @throws InvalidSMSMessageException      if Utils.checkMessageText return false
     * @throws InvalidTelephoneNumberException if Utils.checkTelephoneNumber return false
     */

    public SMSMessage(String telephoneNumber, SMSPacket[] packets)
    {
        this.telephoneNumber = telephoneNumber;
        this.packets = packets;
        for (SMSPacket p: packets)
            message += p.getMessage();
    }

    public SMSMessage(String telephoneNumber, String messageText) throws InvalidSMSMessageException
    {
        this.telephoneNumber = telephoneNumber;
        if(messageText.length() > MAX_MSG_TEXT_LEN)
            throw new InvalidSMSMessageException("text length exceeds maximum allowed", MessageTextState.MESSAGE_TEXT_TOO_LONG);
        this.message = messageText;
        packets = SMSController.getPacketsFromMessage(this);
    }

    /**
     * Telephone Number is the number this message has to be sent to or has been already sent
     *
     * @return the telephone number
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Message Text is the content of the message that can be sent via one or multiple SMS
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    public SMSPacket[] getPackets(){
        return packets;
    }

}
