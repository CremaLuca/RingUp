package com.gruppo4.SMSApp;

/**
 * @author Francesco Bau'
 */

import android.content.Context;

import com.gruppo4.audioUtility.AudioUtilityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AudioUtilityManagerUnitTest extends junit.framework.TestCase {

    @Test
    public void setAlarmVolume_percentage_isTooLow(Context context) {
        try {
            AudioUtilityManager.setAlarmVolume(context, -1);
            Assert.fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Success
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nShould have thrown IllegalArgumentException");
        }
    }

    @Test
    public void setAlarmVolume_percentage_isTooHigh(Context context) {
        try {
            AudioUtilityManager.setAlarmVolume(context, 101);
        } catch (IllegalArgumentException e) {
            // Success
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nShould have thrown IllegalArgumentException");
        }
    }
}
