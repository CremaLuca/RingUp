package com.gruppo4.sms.dataLink;

import org.junit.Before;

public class SMSHandlerUnitTest {

    SMSHandler smsHandler;

    @Before
    public void init() {
        smsHandler = SMSHandler.setup(null, 1);
    }

}
