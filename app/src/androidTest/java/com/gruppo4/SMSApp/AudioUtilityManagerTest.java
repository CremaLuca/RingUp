package com.gruppo4.SMSApp;

/**
 * @author Francesco Bau', Alberto Ursino
 */

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.gruppo4.audioUtility.AudioUtilityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AudioUtilityManagerTest {

    Context context;

    @Before
    public void init() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void setAlarmVolume_percentage_isTooLow() {
        try {
            AudioUtilityManager.setAlarmVolume(context, -1);
            Assert.fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //Success
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nShould have thrown IllegalArgumentException");
        }
    }

    @Test
    public void setAlarmVolume_percentage_isTooHigh() {
        try {
            AudioUtilityManager.setAlarmVolume(context, 101);
        } catch (IllegalArgumentException e) {
            // Success
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nShould have thrown IllegalArgumentException");
        }
    }

    @Test
    public void setMaxAlarmVolume_maxVolume_isOK() {
        try {
            AudioUtilityManager.setMaxAlarmVolume(context);
            //Success
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getStackTrace() + "\nMAX_PERCENTAGE is NOT OK. Please check this value.");
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nIt's NOT supposed to fail like this!");
        }
    }

    @Test
    public void setMinAlarmVolume_minVolume_isOK() {
        try {
            AudioUtilityManager.setMinAlarmVolume(context);
            //Success
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getStackTrace() + "\nMIN_PERCENTAGE is NOT OK. Please check this value.");
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nIt's NOT supposed to fail like this!");
        }
    }

    @Test
    public void getCurrentAlarmVolume_method_isOK() {
        try {
            AudioUtilityManager.getCurrentAlarmVolume(context);
            //Success
        } catch (Exception e) {
            Assert.fail(e.getStackTrace() + "\nIt's NOT supposed to fail!");
        }
    }


}
