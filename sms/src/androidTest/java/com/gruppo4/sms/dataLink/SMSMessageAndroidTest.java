package com.gruppo4.sms.dataLink;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;


public class SMSMessageAndroidTest {

    private static final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private static final String VALID_TEXT = "Test Message";
    private static final String SHOULD_NOT = "Should not have thrown an exception";
    private static final String VALID_NUMBER = "+391111111111";

    /**
     * Testing if getPeer works properly
     */
    @Test
    public void hasSameTelephoneNumber() {
        try {
            Assert.assertEquals(new SMSMessage(context, VALID_NUMBER, VALID_TEXT).getPeer().toString(), VALID_NUMBER);
        } catch (Exception e) {
            Assert.fail(SHOULD_NOT);
        }
    }

    /**
     * Testing if getData works properly
     */
    @Test
    public void hasSameText() {
        try {
            Assert.assertEquals(new SMSMessage(context, VALID_NUMBER, VALID_TEXT).getData(), VALID_TEXT);
        } catch (Exception e) {
            Assert.fail("Should not have thrown an exception");
        }
    }
}
