package com.gruppo4.sms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SMSControllerUnitTest {

    SMSController smsController;

    @Before
    public void init() {
        smsController = SMSController.setup(1);
    }


    @Test
    public void message_received_listener_isNotNull(){
        try {
            smsController.addOnReceiveListener(null);
            Assert.fail("Should have thrown NullPointerException exception");
        }catch(NullPointerException e){
            //Success
        }
    }

}
