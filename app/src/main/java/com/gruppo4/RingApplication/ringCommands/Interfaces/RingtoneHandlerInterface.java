package com.gruppo4.RingApplication.ringCommands.Interfaces;

import android.content.Context;
import android.media.Ringtone;

public interface RingtoneHandlerInterface {

    /**
     * Get the tone of the specified type
     *
     * @param ctx the current application context
     * @return the requested tone
     */
    Ringtone getDefaultTone(Context ctx, int type);

    /**
     * Get the default ringtone
     *
     * @param ctx the current application context
     * @return the actual ringtone
     */
    Ringtone getDefaultRingtone(Context ctx);

    /**
     * Get the default alarm tone
     *
     * @param ctx the current application context
     * @return the actual alarm tone
     */
    Ringtone getDefaultAlarmTone(Context ctx);

    /**
     * Plays a ringtone (overload di ringtonePlay(Ringtone ringtone, final int USAGE_CODE))
     *
     * @param ringtone the default ringtone
     */
    void playRingtone(Ringtone ringtone);

    /**
     * Stops the playing sound
     *
     * @param ringtone the playing sound (ringtone or alarm tone)
     */
    void stopRingtone(Ringtone ringtone);

}
