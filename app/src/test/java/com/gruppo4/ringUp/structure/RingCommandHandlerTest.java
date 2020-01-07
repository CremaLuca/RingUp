package com.gruppo4.ringUp.structure;


import android.util.Log;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.expect;

/**
 * Unit testing of the class RingCommandHandler
 *
 * @author Alberto Ursino
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class RingCommandHandlerTest {

    private static final String SIGNATURE = "ringUp password: ";
    private static final String VALID_NUMBER = "+393443444546";
    private static final String VALID_PASSWORD = "pass";
    private static final String INVALID_SIGNATURE = "ciao" + SIGNATURE;
    private static final String VALID_CONTENT = SIGNATURE + VALID_PASSWORD;
    private static final String WRONG_CONTENT = VALID_PASSWORD;
    private static final SMSPeer SMS_PEER = new SMSPeer(VALID_NUMBER);
    private RingCommandHandler ringCommandHandler = null;
    private SMSMessage smsMessage = new SMSMessage(new SMSPeer(VALID_NUMBER), VALID_CONTENT);

    @Before
    public void init() {
        PowerMock.mockStatic(Log.class);
        ringCommandHandler = RingCommandHandler.getInstance();
    }

    @Test
    public void parseMessage_content_isValid() {
        expect(Log.d("RingCommandHandler", "Message arrived: " + smsMessage.getData())).andReturn(0);
        PowerMock.replay(Log.class);
        Assert.assertNotNull(ringCommandHandler.parseMessage(smsMessage));
        PowerMock.verify(Log.class);
    }

    @Test
    public void parseMessage_content_is_isTooShort() {
        expect(Log.d("RingCommandHandler", "Message arrived: " + WRONG_CONTENT)).andReturn(0);
        expect(Log.d("RingCommandHandler", "The smsMessage received is not long enough, it can't be a right ring command")).andReturn(1);
        PowerMock.replay(Log.class);
        Assert.assertNull(ringCommandHandler.parseMessage(new SMSMessage(new SMSPeer(VALID_NUMBER), WRONG_CONTENT)));
        PowerMock.verify();
    }

    @Test
    public void parseMessage_content_hasInvalidSignature() {
        expect(Log.d("RingCommandHandler", "Message arrived: " + INVALID_SIGNATURE)).andReturn(0);
        expect(Log.d("RingCommandHandler", "The ring command received doesn't contain the right signature")).andReturn(1);
        PowerMock.replay(Log.class);
        Assert.assertNull(ringCommandHandler.parseMessage(new SMSMessage(new SMSPeer(VALID_NUMBER), INVALID_SIGNATURE)));
        PowerMock.verify();
    }

    @Test
    public void parseMessage_ringCommandPasswords_areEquals() {
        Assert.assertEquals(new RingCommand(SMS_PEER, VALID_PASSWORD).getPassword(), ringCommandHandler.parseMessage(smsMessage).getPassword());
    }

    @Test
    public void parseMessage_ringCommandPeers_areEquals() {
        Assert.assertEquals(new RingCommand(SMS_PEER, VALID_CONTENT).getPeer(), ringCommandHandler.parseMessage(smsMessage).getPeer());
    }

}