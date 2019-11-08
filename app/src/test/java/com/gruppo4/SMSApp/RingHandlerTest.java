package com.gruppo4.SMSApp;

import com.gruppo4.SMSApp.ringCommands.RingCommand;
import com.gruppo4.SMSApp.ringCommands.RingHandler;
import com.gruppo4.sms.dataLink.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RingHandlerTest {

    private static final String SEPARATOR_CHARACTER = (char)0x02 + "";
    private static final String VALID_NUMBER = "+391111111111";
    private static final String VALID_PASSWORD = "Hello I'm ThE PaSsWoRd";
    private static final String VALID_TEXT = "Hi i'm Albert";
    private static final String VALID_CONTENT = VALID_TEXT+SEPARATOR_CHARACTER+VALID_PASSWORD;
    private static final SMSPeer SMS_PEER = new SMSPeer(VALID_NUMBER);
    private static final String SHOULD_NOT = "It should not have thrown an exception";

    @Before
    public void parseString_isOk(){
        try{
            RingHandler.parseString(SMS_PEER, VALID_CONTENT);
        }catch(Exception e){
            Assert.fail(SHOULD_NOT);
        }
    }

    @Test
    public void parseString_ringCommandPasswords_areEquals() {
        Assert.assertEquals(RingHandler.parseString(SMS_PEER, VALID_CONTENT).getPassword(), new RingCommand(SMS_PEER, VALID_PASSWORD).getPassword());
    }

    @Test
    public void parseString_ringCommandPeers_areEquals() {
        Assert.assertEquals(RingHandler.parseString(SMS_PEER, VALID_CONTENT).getSender(), new RingCommand(SMS_PEER, VALID_CONTENT).getSender());
    }
}