package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Test;

public class SMSHandlerTest extends Variables {

    @Test
    public void checkApplicationCodeIsValid_applicationCode_tooBig() {
        Assert.assertEquals(SMSHandler.checkApplicationCodeIsValid(TOO_BIG_APPLICATION_CODE), false);
    }

    @Test
    public void checkApplicationCodeIsValid_applicationCode_tooSmall() {
        Assert.assertEquals(SMSHandler.checkApplicationCodeIsValid(TOO_SMALL_APPLICATION_CODE), false);
    }

    @Test
    public void checkApplicationCodeIsValid_applicationCode_isValid() {
        Assert.assertEquals(SMSHandler.checkApplicationCodeIsValid(VALID_APPLICATION_CODE), true);
    }

}