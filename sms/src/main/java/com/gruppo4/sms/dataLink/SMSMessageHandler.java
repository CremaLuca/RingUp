package com.gruppo4.sms.dataLink;

import android.util.Log;

import com.gruppo4.communication.dataLink.MessageHandler;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;

/**
 * @author Luca Crema, Marco Mariotto
 */

public class SMSMessageHandler extends MessageHandler<SMSMessage> {

    public static final String SPLIT_CHARACTER = "_";
    public static final String HIDDEN_CHARACTER = (char) 0x02 + "";
    private static SMSMessageHandler instance;
    private static final String EXAMPLE_TELEPHONE_NUMBER = "+393455543456";

    public static SMSMessageHandler getInstance() {
        if (instance == null)
            instance = new SMSMessageHandler();
        return instance;
    }

    @Override
    protected SMSMessage parseMessage(String data, String phoneNumber) {
        String[] splitData = data.split(SPLIT_CHARACTER, 2);
        if (splitData.length < 2)
            return null;
        //First part must be (1 + 3 = 4) characters long at max
        if (splitData[0].length() > 4)
            return null;

        //First character must be the hidden char
        if (!splitData[0].startsWith(HIDDEN_CHARACTER))
            return null;

        //First part after hidden char must contain ONLY numbers, that is the application id
        if (!splitData[0].substring(1).matches("[0-9]+"))
            return null;

        int appID = Integer.parseInt(splitData[0].substring(1));//we have to remove the special character first
        try {
            return new SMSMessage(appID, new SMSPeer(phoneNumber), splitData[1]);
        } catch (InvalidTelephoneNumberException te) {
            Log.e("SMSMessageHandler", "Parsed message with illegal phone number, reason: " + te.getState());
        } catch (InvalidSMSMessageException me) {
            Log.e("SMSMessageHandler", "Parsed message with illegal data, reason: " + me.getState());
        }
        return null;
    }

    @Override
    protected String getOutput(SMSMessage message) {
        return HIDDEN_CHARACTER + message.getApplicationID() + SPLIT_CHARACTER + message.getData();
    }

    public boolean isLibraryMessage(String content) {
        return parseMessage(content, EXAMPLE_TELEPHONE_NUMBER) != null;
    }
}
