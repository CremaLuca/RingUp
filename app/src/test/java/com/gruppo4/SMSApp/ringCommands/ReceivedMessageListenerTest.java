package com.gruppo4.SMSApp.ringCommands;

import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import org.junit.Before;
import org.junit.Test;

public class ReceivedMessageListenerTest {

    private static final String SPLIT_CHARACTER = "_";
    private static final String VALID_NUMBER = "+391111111111";
    private static final String VALID_COMMAND = SPLIT_CHARACTER + "PASSWORD";
    private static SMSMessage smsMessage;

    @Before
    public void init() throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        smsMessage = new SMSMessage(1, new SMSPeer(VALID_NUMBER), VALID_COMMAND);
    }

    @Test
    public void onMessageReceived_validCommand(){

    }
}