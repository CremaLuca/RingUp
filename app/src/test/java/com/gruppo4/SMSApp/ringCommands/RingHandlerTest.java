package com.gruppo4.SMSApp.ringCommands;

import com.gruppo4.sms.dataLink.SMSPeer;
import org.junit.Assert;
import org.junit.Test;

public class RingHandlerTest {

    private static final String SPLIT_CHARACTER = "_";
    private static final String VALID_NUMBER = "+391111111111";
    private static final String VALID_PASSWORD = "pass";
    private static final String VALID_CONTENT = SPLIT_CHARACTER + VALID_PASSWORD;
    private static final String WRONG_CONTENT = VALID_PASSWORD;
    private static SMSPeer SMS_PEER = new SMSPeer(VALID_NUMBER);

    @Test
    public void parseContent_content_isValid() {
        Assert.assertNotEquals(null, RingHandler.parseContent(SMS_PEER, VALID_CONTENT));
    }

    @Test
    public void parseContent_content_isNotValid() {
        Assert.assertEquals(null, RingHandler.parseContent(SMS_PEER, WRONG_CONTENT));
    }

    @Test
    public void parseContent_ringCommandPasswords_areEquals() {
        Assert.assertEquals(new RingCommand(SMS_PEER, VALID_PASSWORD).getPassword(), RingHandler.parseContent(SMS_PEER, VALID_CONTENT).getPassword());
    }

    @Test
    public void parseContent_ringCommandPeers_areEquals() {
        Assert.assertEquals(new RingCommand(SMS_PEER, VALID_CONTENT).getPeer(), RingHandler.parseContent(SMS_PEER, VALID_CONTENT).getPeer());
    }
}