package com.gruppo4.sms;

import org.junit.Before;

public class SMSControllerUnitTest {

    SMSController smsController;

    @Before
    public void init() {
        smsController = SMSController.setup(null, 1);
    }

}
