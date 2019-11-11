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
            Assert.fail("Should have thrown IllegalArgumentException");
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
}
