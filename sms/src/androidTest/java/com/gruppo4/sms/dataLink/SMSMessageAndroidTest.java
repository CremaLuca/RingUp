package com.gruppo4.sms.dataLink;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.gruppo4.communication.Peer;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SMSMessageAndroidTest {

    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    SMSPeer peer = new SMSPeer("+391111111111"); //A right number
    SMSMessage message = null;

    @Before
    public void init() {
        try {
            message = new SMSMessage(context, peer, "Test message");
        } catch (Exception e) {
            Assert.fail("Should not have thrown an exception");
        }
    }

    @Test
    public void sms_hasSameTelephoneNumber(){
        try {
            message = new SMSMessage(context, peer, "Test message");
        }catch(Exception e){
            Assert.fail("Should not have thrown an exception");
        }
        Assert.assertEquals(message.getPeer().toString(),"+391111111111");
    }

    @Test
    public void sms_hasSameText(){
        try {
            message = new SMSMessage(context, peer, "Test message");
        }catch(Exception e){
            Assert.fail("Should not have thrown an exception");
        }
        Assert.assertEquals(message.getData(),"Test message");
    }
}
