package com.gruppo4.sms;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.gruppo4.sms.dataLink.SMSHandler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SMSHandlerAndroidTest {

    private static final int VALID_TEST_APP_ID = 69;
    private static final int INVALID_TEST_APP_ID_1 = -56;
    private static final int INVALID_TEST_APP_ID_2 = 1136;
    Context context = null;

    @Before
    public void init() {
        this.context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void setup_applicationCode_isEquals() {
        SMSHandler.setup(context, VALID_TEST_APP_ID);
        Assert.assertEquals(SMSHandler.getApplicationCode(context), VALID_TEST_APP_ID);
    }

    @Test
    public void checkApplicationCodeIsValid_applicationCode_isValid() {
        Assert.assertTrue(SMSHandler.checkApplicationCodeIsValid(VALID_TEST_APP_ID));
    }

    @Test
    public void checkApplicationCodeIsValid_applicationCode_isTooSmall() {
        Assert.assertFalse(SMSHandler.checkApplicationCodeIsValid(INVALID_TEST_APP_ID_1));
    }

    @Test
    public void checkApplicationCodeIsValid_applicationCode_isTooBig() {
        Assert.assertFalse(SMSHandler.checkApplicationCodeIsValid(INVALID_TEST_APP_ID_2));
    }
}
