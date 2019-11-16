package com.gruppo4.audioUtility;

/**
 * @author Francesco Bau'
 */

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import static android.content.Context.AUDIO_SERVICE;


public class AudioUtilityManager {

    public static final int MAX_PERCENTAGE = 100;
    public static final int MIN_PERCENTAGE = 0;

    public enum AUMStream {
        ALARM,
        RING,
        MUSIC
    }

    /**
     * @param context The current Context.
     * @return The current AudioManager instance.
     */
    protected static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    /**
     * @param context The current Context.
     * @param AUMStream  The chosen AUMStream (should be ALARM, RING or MUSIC, otherwise an exception is thrown).
     * @return The current AUMStream Volume (in percentage).
     */
    public static int getVolume(Context context, AUMStream AUMStream) {
        int currentVolume = getAudioManager(context).getStreamVolume(getStream(context, AUMStream));

        int maxVolume = getMaxVolume(context, AUMStream);
        return Math.round(MAX_PERCENTAGE * currentVolume / maxVolume);
    }

    /**
     * @param context The current Context.
     * @param AUMStream  The chosen AUMStream (should be ALARM, RING or MUSIC, otherwise an exception is thrown).
     * @return The maximum AUMStream volume (real value).
     */
    private static int getMaxVolume(Context context, AUMStream AUMStream) {
        return getAudioManager(context).getStreamMaxVolume(getStream(context, AUMStream));
    }

    /**
     * Sets up the AUMStream Volume, given a certain percentage.
     *
     * @param context    The current Context.
     * @param AUMStream     The chosen AUMStream (should be ALARM, RING or MUSIC, otherwise an exception is thrown).
     * @param percentage Target volume (expressed in %).
     * @throws IllegalArgumentException if percentage is not between 0 and 100.
     */
    public static void setVolume(Context context, AUMStream AUMStream, int percentage) throws IllegalArgumentException {
        if (percentage < MIN_PERCENTAGE)
            throw new IllegalArgumentException(" Your value is too low. Please insert a value between 0 and 100.");
        else if (percentage > MAX_PERCENTAGE) {
            throw new IllegalArgumentException(" Your value is too high. Please insert a value between 0 and 100.");
        }
        int maxVolume = getMaxVolume(context, AUMStream);

        // Calculate the real value of the new volume
        int newVolume = maxVolume * percentage;
        newVolume = Math.round(newVolume / MAX_PERCENTAGE);

        // Sets the Volume.
        getAudioManager(context).setStreamVolume(
                getStream(context, AUMStream),
                newVolume,
                AudioManager.FLAG_SHOW_UI
        );


    }

    /**
     * Sets up the AUMStream Volume to its maximum value (in percentage).
     *
     * @param context The current Context.
     * @param AUMStream  The chosen AUMStream (should be ALARM, RING or MUSIC, otherwise an exception is thrown).
     */
    public static void setMaxVolume(Context context, AUMStream AUMStream) {
        setVolume(context, AUMStream, MAX_PERCENTAGE);
    }

    /**
     * Sets up the AUMStream Volume to its minimum value (in percentage).
     *
     * @param context The current Context.
     * @param AUMStream  The chosen AUMStream (should be ALARM, RING or MUSIC, otherwise an exception is thrown).
     */
    public static void setMinVolume(Context context, AUMStream AUMStream) {
        setVolume(context, AUMStream, MIN_PERCENTAGE);
    }

    /**
     * @param context The current Context.
     * @param AUMStream  The chosen AUMStream (should be ALARM, RING or MUSIC, otherwise an exception is thrown).
     * @return The constant representing the AUMStream.
     * @throws IllegalArgumentException if AUMStream is not valid (It shouldn't happen the enum).
     */
    private static int getStream(Context context, AUMStream AUMStream) throws IllegalArgumentException {

        switch (AUMStream) {
            case ALARM:
                return AudioManager.STREAM_ALARM;
            case RING:
                return AudioManager.STREAM_RING;
            case MUSIC:
                return AudioManager.STREAM_MUSIC;
            default:
                throw new IllegalArgumentException("\nIllegal AUMStream. It should be ALARM, RING or MUSIC .");
        }
    }

    /**
     * @param context The current Context.
     * @return The current Vibrator instance.
     */
    protected static Vibrator getVibrator(Context context) {
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * Makes the target device vibrate.
     * @param context The current Context.
     */
    public static void singleVibrate(Context context){
        // Vibrate for 5000 milliseconds
        // HARDWIRING
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getVibrator(context).vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            getVibrator(context).vibrate(5000);
        }
    }

    /**
     * Makes the target device vibrate.
     * @param context The current Context.
     */
    public static void multipleVibrate(Context context){
        // Multiple vibration works from API 26+, otherwise it would do the same thing done by singleVibrate(Context).
        // HARDWIRING
        long[] x = {500,500,500,500};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getVibrator(context).vibrate(VibrationEffect.createWaveform(x, 1));
        } else {
            //deprecated in API 26
            getVibrator(context).vibrate(5000);
        }
    }


}
//TODO TESTING
