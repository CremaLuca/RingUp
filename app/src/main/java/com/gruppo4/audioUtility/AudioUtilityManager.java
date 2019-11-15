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

    public enum Stream {
        ALARM,
        RING,
        MUSIC
    }

    /**
     * @param context The current Context
     * @return The current AudioManager instance.
     */
    protected static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    /**
     * @param context The current Context
     * @param stream  The chosen Stream (should be ALARM, RING or MUSIC, otherwise an exception is thrown)
     * @return The current Stream Volume (in percentage).
     */
    public static int getVolume(Context context, Stream stream) {
        int currentVolume = getAudioManager(context).getStreamVolume(getStream(context, stream));

        int maxVolume = getMaxVolume(context, stream);
        return Math.round(MAX_PERCENTAGE * currentVolume / maxVolume);
    }

    /**
     * @param context The current Context
     * @param stream  The chosen Stream (should be ALARM, RING or MUSIC, otherwise an exception is thrown)
     * @return The maximum Stream volume (real value).
     */
    private static int getMaxVolume(Context context, Stream stream) {
        return getAudioManager(context).getStreamMaxVolume(getStream(context, stream));
    }

    /**
     * Sets up the Stream Volume, given a certain percentage.
     *
     * @param context    The current Context
     * @param stream     The chosen Stream (should be ALARM, RING or MUSIC, otherwise an exception is thrown)
     * @param percentage Target volume (expressed in %).
     * @throws IllegalArgumentException if percentage is not between 0 and 100.
     */
    public static void setVolume(Context context, Stream stream, int percentage) throws IllegalArgumentException {
        if (percentage < MIN_PERCENTAGE)
            throw new IllegalArgumentException(" Your value is too low. Please insert a value between 0 and 100.");
        else if (percentage > MAX_PERCENTAGE) {
            throw new IllegalArgumentException(" Your value is too high. Please insert a value between 0 and 100.");
        }
        int maxVolume = getMaxVolume(context, stream);

        // Calculate the real value of the new volume
        int newVolume = maxVolume * percentage;
        newVolume = Math.round(newVolume / MAX_PERCENTAGE);

        // Sets the Volume.
        getAudioManager(context).setStreamVolume(
                getStream(context, stream),
                newVolume,
                AudioManager.FLAG_SHOW_UI
        );


    }

    /**
     * Sets up the Stream Volume to its maximum value (in percentage).
     *
     * @param context The current Context
     * @param stream  The chosen Stream (should be ALARM, RING or MUSIC, otherwise an exception is thrown)
     */
    public static void setMaxVolume(Context context, Stream stream) {
        setVolume(context, stream, MAX_PERCENTAGE);
    }

    /**
     * Sets up the Stream Volume to its minimum value (in percentage).
     *
     * @param context The current Context
     * @param stream  The chosen Stream (should be ALARM, RING or MUSIC, otherwise an exception is thrown)
     */
    public static void setMinVolume(Context context, Stream stream) {
        setVolume(context, stream, MIN_PERCENTAGE);
    }

    /**
     * @param context The current Context
     * @param stream  The chosen Stream (should be ALARM, RING or MUSIC, otherwise an exception is thrown)
     * @return The constant representing the stream.
     * @throws IllegalArgumentException if stream is not valid (It shouldn't happen the enum).
     */
    private static int getStream(Context context, Stream stream) throws IllegalArgumentException {

        switch (stream) {
            case ALARM:
                return AudioManager.STREAM_ALARM;
            case RING:
                return AudioManager.STREAM_RING;
            case MUSIC:
                return AudioManager.STREAM_MUSIC;
            default:
                throw new IllegalArgumentException("\nIllegal Stream. It should be ALARM, RING or MUSIC .");
        }
    }


}
//TODO TESTING