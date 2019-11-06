package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SMSHandlerTest extends Variables {

    @Test
    public void checkApplicationCodeIsValid_applicationCode_tooBig() {
        Assert.assertEquals(smsHandler.checkApplicationCodeIsValid(TOO_BIG_APPLICATION_CODE), false);
    }

    @Test
    public void checkApplicationCodeIsValid_applicationCode_tooSmall(){
        Assert.assertEquals(smsHandler.checkApplicationCodeIsValid(TOO_SMALL_APPLICATION_CODE), false);
    }

    @Test
    public void checkApplicationCodeIsValid_applicationCode_isValid(){
        Assert.assertEquals(smsHandler.checkApplicationCodeIsValid(VALID_APPLICATION_CODE), true);
    }
    
}