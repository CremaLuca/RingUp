package com.gruppo4.sms.dataLink;

import android.util.Log;

import com.gruppo4.communication.dataLink.MessageHandler;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;

public class SMSMessageHandler extends MessageHandler<SMSMessage> {

    public static final String SPLITER_CHARACTER = "_";
    private static SMSMessageHandler instance;

    public static SMSMessageHandler getInstance() {
        if (instance == null)
            instance = new SMSMessageHandler();
        return instance;
    }

    @Override
    protected SMSMessage parseMessage(String data, String peerData) {
        String[] splitData = data.split(SPLITER_CHARACTER, 2);
        if (splitData.length < 2)
            return null;
        //First part must contain ONLY numbers, that is the application id
        if (!splitData[0].matches("[0-9]+"))
            return null;
        //First part must be (1 + 3 = 4) characters long at max
        if (splitData[0].length() > 4)
            return null;
        int appID = Integer.parseInt(splitData[0].substring(1));//we have to remove the special character first
        try {
            return new SMSMessage(appID, new SMSPeer(peerData), splitData[1]);
        } catch (InvalidTelephoneNumberException te) {
            Log.e("SMSMessageHandler", "Parsed message with illegal phone number, reason: " + te.getState());
        } catch (InvalidSMSMessageException me) {
            Log.e("SMSMessageHandler", "Parsed message with illegal data, reason: " + me.getState());
        }
        return null;
    }

    @Override
    protected String getOutput(SMSMessage message) {
        return 0x02 + message.getApplicationID() + SPLITER_CHARACTER + message.getData();
    }
}
