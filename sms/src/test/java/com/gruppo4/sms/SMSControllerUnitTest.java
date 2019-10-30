package com.gruppo4.sms;

import org.junit.Before;

public class SMSControllerUnitTest {

    SMSController smsController;

    @Before
    public void init() {
        SMSController.init(null, 1);
        smsController = SMSController.getInstance();
    }

}
