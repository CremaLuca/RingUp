package com.gruppo4.RingApplication.structure;

import com.gruppo4.sms.dataLink.SMSPeer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit testing of the class RingCommandHandler
 *
 * @author Alberto Ursino
 */
public class RingCommandHandlerTest {

    private static final String SPLIT_CHARACTER = "_";
    private static final String VALID_NUMBER = "+391111111111";
    private static final String VALID_PASSWORD = "pass";
    private static final String VALID_CONTENT = SPLIT_CHARACTER + VALID_PASSWORD;
    private static final String WRONG_CONTENT = VALID_PASSWORD;
    private static SMSPeer SMS_PEER = new SMSPeer(VALID_NUMBER);
    private RingCommandHandler ringCommandHandler = null;

    @Before
    public void init() {
        ringCommandHandler = RingCommandHandler.getInstance();
    }

    @Test
    public void parseContent_content_isValid() {
        Assert.assertNotEquals(null, ringCommandHandler.parseContent(SMS_PEER, VALID_CONTENT));
    }

    @Test
    public void parseContent_content_isNotValid() {
        Assert.assertEquals(null, ringCommandHandler.parseContent(SMS_PEER, WRONG_CONTENT));
    }

    @Test
    public void parseContent_ringCommandPasswords_areEquals() {
        Assert.assertEquals(new RingCommand(SMS_PEER, VALID_PASSWORD).getPassword(), ringCommandHandler.parseContent(SMS_PEER, VALID_CONTENT).getPassword());
    }

    @Test
    public void parseContent_ringCommandPeers_areEquals() {
        Assert.assertEquals(new RingCommand(SMS_PEER, VALID_CONTENT).getPeer(), ringCommandHandler.parseContent(SMS_PEER, VALID_CONTENT).getPeer());
    }

}