package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Test;

public class SMSHandlerTest extends Variables {

    @Test
    public void checkApplicationCodeIsValid_applicationCode_tooBig() {
        Assert.assertFalse(SMSHandler.checkApplicationCodeIsValid(TOO_BIG_APPLICATION_CODE));
    }

    @Test
    public void checkApplicationCodeIsValid_applicationCode_tooSmall() {
        Assert.assertFalse(SMSHandler.checkApplicationCodeIsValid(TOO_SMALL_APPLICATION_CODE));
    }

    @Test
    public void checkApplicationCodeIsValid_applicationCode_isValid() {
        Assert.assertFalse(SMSHandler.checkApplicationCodeIsValid(VALID_APPLICATION_CODE));
    }

}