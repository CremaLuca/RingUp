package com.gruppo4.SMSApp;

import com.gruppo4.SMSApp.ringCommands.RingCommand;
import com.gruppo4.SMSApp.ringCommands.RingHandler;
import com.gruppo4.sms.dataLink.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RingHandlerTest {

    private static final String INVISIBLE_CHARACTER = (char) 0x02 + "";
    private static final String VALID_NUMBER = "+391111111111";
    private static final String VALID_PASSWORD = "pass";
    private static final String VALID_TEXT = "command";
    private static final String VALID_CONTENT = INVISIBLE_CHARACTER + VALID_TEXT + INVISIBLE_CHARACTER + VALID_PASSWORD;
    private static final String WRONG_CONTENT = "ciao";
    private static final String ONE_INV_CHAR_ONLY = INVISIBLE_CHARACTER + VALID_TEXT + VALID_PASSWORD;
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
    public void parseContent_content_hasOneInvChar(){
        Assert.assertEquals(null, RingHandler.parseContent(SMS_PEER, ONE_INV_CHAR_ONLY));
    }

    @Test
    public void parseContent_ringCommandPasswords_areEquals() {
        Assert.assertEquals(new RingCommand(SMS_PEER, VALID_PASSWORD).getPassword(), RingHandler.parseContent(SMS_PEER, VALID_CONTENT).getPassword());
    }

    @Test
    public void pparseContent_ringCommandPeers_areEquals() {
        Assert.assertEquals(new RingCommand(SMS_PEER, VALID_CONTENT).getSender(), RingHandler.parseContent(SMS_PEER, VALID_CONTENT).getSender());
    }
}