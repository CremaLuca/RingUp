package com.gruppo4.SMSApp;

/**
 * @author Francesco Bau'
 */
import android.content.Context;

import com.gruppo4.audioUtility.AudioUtilityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AudioUtilityManagerUnitTest extends junit.framework.TestCase{
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void percentage_isTooLow(Context context){
        try{
            AudioUtilityManager.setAlarmVolume(context,-1);
            Assert.fail("Should have thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){
            //Success
        }
        catch (Exception x){
            Assert.fail(x.getStackTrace()+"\nShould have thrown IllegalArgumentException");
        }
    }
    @Test
    public void percentage_isTooHigh(Context context){
        AudioUtilityManager.setAlarmVolume(context,101);
    }
}
