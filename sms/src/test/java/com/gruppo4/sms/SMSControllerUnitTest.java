package com.gruppo4.sms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SMSControllerUnitTest {

    @Before
    public void init() {

    }

    @Test
    public void message_sent_listener_isNotNull(){
        try {
            SMSController.addOnSentListener(null);
            Assert.fail("Should have thrown NullPointerException exception");
        }catch(NullPointerException e){
            //Success
        }
    }

    @Test
    public void message_received_listener_isNotNull(){
        try {
            SMSController.addOnReceiveListener(null);
            Assert.fail("Should have thrown NullPointerException exception");
        }catch(NullPointerException e){
            //Success
        }
    }

}
