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
            AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.ALARM, -1);
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
            AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.ALARM, 101);
        } catch (IllegalArgumentException e) {
            // Success
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nShould have thrown IllegalArgumentException");
        }
    }

    @Test
    public void setAlarmVolumeToMax_maxVolume_isOK() {
        try {
            AudioUtilityManager.setVolumeToMax(context, AudioUtilityManager.AUMStream.ALARM);
            //Success
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getStackTrace() + "\nMAX_PERCENTAGE is NOT OK. Please check this value.");
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nIt's NOT supposed to fail like this!");
        }
    }

    @Test
    public void setAlarmVolumeToMin_minVolume_isOK() {
        try {
            AudioUtilityManager.setVolumeToMin(context, AudioUtilityManager.AUMStream.ALARM);
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
            AudioUtilityManager.getVolume(context, AudioUtilityManager.AUMStream.ALARM);
            //Success
        } catch (Exception e) {
            Assert.fail(e.getStackTrace() + "\nIt's NOT supposed to fail!");
        }
    }

//-----------------------------------------------------------------

    @Test
    public void setMusicVolume_percentage_isTooLow() {
        try {
            AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.ALARM, -1);
            Assert.fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //Success
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nShould have thrown IllegalArgumentException");
        }
    }

    @Test
    public void setMusicVolume_percentage_isTooHigh() {
        try {
            AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.MUSIC, 101);
        } catch (IllegalArgumentException e) {
            // Success
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nShould have thrown IllegalArgumentException");
        }
    }

    @Test
    public void setMusicVolumeToMax_maxVolume_isOK() {
        try {
            AudioUtilityManager.setVolumeToMax(context, AudioUtilityManager.AUMStream.MUSIC);
            //Success
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getStackTrace() + "\nMAX_PERCENTAGE is NOT OK. Please check this value.");
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nIt's NOT supposed to fail like this!");
        }
    }

    @Test
    public void setMusicVolumeToMin_minVolume_isOK() {
        try {
            AudioUtilityManager.setVolumeToMin(context, AudioUtilityManager.AUMStream.MUSIC);
            //Success
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getStackTrace() + "\nMIN_PERCENTAGE is NOT OK. Please check this value.");
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nIt's NOT supposed to fail like this!");
        }
    }

    @Test
    public void getCurrentMusicVolume_method_isOK() {
        try {
            AudioUtilityManager.getVolume(context, AudioUtilityManager.AUMStream.MUSIC);
            //Success
        } catch (Exception e) {
            Assert.fail(e.getStackTrace() + "\nIt's NOT supposed to fail!");
        }
    }

//-----------------------------------------------------------------

    @Test
    public void setRingVolume_percentage_isTooLow() {
        try {
            AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.RING, -1);
            Assert.fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //Success
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nShould have thrown IllegalArgumentException");
        }
    }

    @Test
    public void setRingVolume_percentage_isTooHigh() {
        try {
            AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.RING, 101);
        } catch (IllegalArgumentException e) {
            // Success
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nShould have thrown IllegalArgumentException");
        }
    }

    @Test
    public void setRingVolumeToMax_maxVolume_isOK() {
        try {
            AudioUtilityManager.setVolumeToMax(context, AudioUtilityManager.AUMStream.RING);
            //Success
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getStackTrace() + "\nMAX_PERCENTAGE is NOT OK. Please check this value.");
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nIt's NOT supposed to fail like this!");
        }
    }

    @Test
    public void setRingVolumeToMin_minVolume_isOK() {
        try {
            AudioUtilityManager.setVolumeToMin(context, AudioUtilityManager.AUMStream.RING);
            //Success
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getStackTrace() + "\nMIN_PERCENTAGE is NOT OK. Please check this value.");
        } catch (Exception x) {
            Assert.fail(x.getStackTrace() + "\nIt's NOT supposed to fail like this!");
        }
    }

    @Test
    public void getCurrentRingVolume_method_isOK() {
        try {
            AudioUtilityManager.getVolume(context, AudioUtilityManager.AUMStream.RING);
            //Success
        } catch (Exception e) {
            Assert.fail(e.getStackTrace() + "\nIt's NOT supposed to fail!");
        }
    }

//-----------------------------------------------------------------


}
