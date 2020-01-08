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
import static com.gruppo4.ringUp.structure.RingCommandHandler.CLASS_TAG;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

/**
 * Unit testing of the class RingCommandHandler
 *
 * @author Alberto Ursino
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class RingCommandHandlerTest {

    private static final String SIGNATURE = "ringUp password: ";
    private static final String VALID_NUMBER = "+393443444546";
    private static final String VALID_PASSWORD = "pass";
    private static final String INVALID_SIGNATURE = "ciao" + SIGNATURE;
    private static final String VALID_CONTENT = SIGNATURE + VALID_PASSWORD;
    private static final SMSPeer SMS_PEER = new SMSPeer(VALID_NUMBER);
    private static RingCommandHandler ringCommandHandler;
    private static final SMSMessage VALID_SMS_MESSAGE = new SMSMessage(new SMSPeer(VALID_NUMBER), VALID_CONTENT);
    private static final RingCommand VALID_RING_COMMAND = new RingCommand(new SMSPeer(VALID_NUMBER), VALID_PASSWORD);

    @Before
    public void init() {
        PowerMock.mockStatic(Log.class);
        ringCommandHandler = RingCommandHandler.getInstance();
    }

    @Test
    public void parseMessage_content_isValid() {
        expect(Log.d(CLASS_TAG, "Message received: " + VALID_SMS_MESSAGE.getData())).andReturn(0);
        replay(Log.class);
        Assert.assertNotNull(ringCommandHandler.parseMessage(VALID_SMS_MESSAGE));
        verify(Log.class);
    }

    @Test
    public void parseMessage_content_is_isTooShort() {
        SMSMessage smsMessage = new SMSMessage(new SMSPeer(VALID_NUMBER), VALID_PASSWORD);
        expect(Log.d(CLASS_TAG, "Message received: " + smsMessage.getData())).andReturn(0);
        expect(Log.d(CLASS_TAG, "The smsMessage received is not long enough, it can't be a right ring command")).andReturn(1);
        replay(Log.class);
        Assert.assertNull(ringCommandHandler.parseMessage(smsMessage));
        verify(Log.class);
    }

    @Test
    public void parseMessage_content_hasInvalidSignature() {
        SMSMessage smsMessage = new SMSMessage(new SMSPeer(VALID_NUMBER), INVALID_SIGNATURE);
        expect(Log.d(CLASS_TAG, "Message received: " + smsMessage.getData())).andReturn(0);
        expect(Log.d(CLASS_TAG, "The ring command received does not contain the right signature")).andReturn(1);
        replay(Log.class);
        Assert.assertNull(ringCommandHandler.parseMessage(smsMessage));
        verify(Log.class);
    }

    @Test
    public void parseMessage_ringCommand_tests() {
        expect(Log.d(CLASS_TAG, "Message received: " + VALID_SMS_MESSAGE.getData())).andReturn(0);
        expect(Log.d(CLASS_TAG, "Message received: " + VALID_SMS_MESSAGE.getData())).andReturn(1);
        replay(Log.class);
        Assert.assertEquals(new RingCommand(SMS_PEER, VALID_PASSWORD).getPassword(), ringCommandHandler.parseMessage(VALID_SMS_MESSAGE).getPassword());
        Assert.assertEquals(new RingCommand(SMS_PEER, VALID_CONTENT).getPeer(), ringCommandHandler.parseMessage(VALID_SMS_MESSAGE).getPeer());
        verify(Log.class);
    }

    @Test
    public void parseContent_tests() {
        //Same password
        Assert.assertEquals(VALID_PASSWORD, ringCommandHandler.parseCommand(VALID_RING_COMMAND).getData());
        //Same peer
        Assert.assertEquals(VALID_SMS_MESSAGE.getPeer(), ringCommandHandler.parseCommand(VALID_RING_COMMAND).getPeer());
    }
}