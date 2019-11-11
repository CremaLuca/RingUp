package com.gruppo4.audioUtility;

/**
 * @author Francesco Bau'
 */

import android.content.Context;
import android.media.AudioManager;

import static android.content.Context.AUDIO_SERVICE;


public class AudioUtilityManager {

    public static final int MAX_PERCENTAGE = 100;
    public static final int MIN_PERCENTAGE = 0;

    /**
     * @param context
     * @return The current Alarm Volume (in percentage).
     */
    public static int getCurrentAlarmVolume(Context context) {

        int currentVolume = getAudioManager(context).getStreamVolume(AudioManager.STREAM_ALARM);

        int maxVolume = getMaxAlarmVolume(context);
        return Math.round(MAX_PERCENTAGE * currentVolume / maxVolume);
    }

    /**
     * @param context
     * @return The maximum Alarm volume (real value).
     */
    private static int getMaxAlarmVolume(Context context) {
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }

    /**
     * @param context
     * @return The current AudioManager instance, determined by a certain Context.
     */
    protected static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    /**
     * @param context
     * @param percentage
     * @throws IllegalArgumentException if percentage is not between 0 and 100.
     *                                  Sets up the Alarm Volume, given a certain percentage.
     */
    public static void setAlarmVolume(Context context, int percentage) throws IllegalArgumentException {

        if (percentage < MIN_PERCENTAGE)
            throw new IllegalArgumentException(" Your value is too low. Please insert a value between 0 and 100.");
        else if (percentage > MAX_PERCENTAGE) {
            throw new IllegalArgumentException(" Your value is too high. Please insert a value between 0 and 100.");
        }

        int maxVolume = getMaxAlarmVolume(context);

        // Calculate the real value of the new volume
        int newVolume = maxVolume * percentage;
        newVolume = Math.round(newVolume / MAX_PERCENTAGE);

        // Set the Alarm volume
        getAudioManager(context).setStreamVolume(
                AudioManager.STREAM_ALARM,
                newVolume,
                AudioManager.FLAG_SHOW_UI
        );

    }

    /**
     * @param context Sets up the Alarm Volume to its maximum value (in percentage).
     */
    public static void setMaxAlarmVolume(Context context) {
        setAlarmVolume(context, MAX_PERCENTAGE);
    }

    /**
     * @param context Sets up the Alarm Volume to its minimum value (in percentage).
     */
    public static void setMinAlarmVolume(Context context) {
        setAlarmVolume(context, MIN_PERCENTAGE);
    }

}
