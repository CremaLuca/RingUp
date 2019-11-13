package com.gruppo4.audioUtility;

/**
 * @author Francesco Bau'
 */

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import static android.content.Context.AUDIO_SERVICE;


public class AudioUtilityManager {

    public static final int MAX_PERCENTAGE = 100;
    public static final int MIN_PERCENTAGE = 0;

    /**
     * @param context The current Context
     * @return The current AudioManager instance.
     */
    protected static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    /**
     * @param context The current Context
     * @return The current Alarm Volume (in percentage).
     */
    public static int getCurrentAlarmVolume(Context context) {
        int currentVolume = getAudioManager(context).getStreamVolume(AudioManager.STREAM_ALARM);

        int maxVolume = getMaxAlarmVolume(context);
        return Math.round(MAX_PERCENTAGE * currentVolume / maxVolume);
    }

    /**
     * @param context The current Context
     * @return The maximum Alarm volume (real value).
     */
    private static int getMaxAlarmVolume(Context context) {
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }

    /**
     * Sets up the Alarm Volume, given a certain percentage.
     * @param context The current Context
     * @param percentage Target volume (expressed in %).
     * @throws IllegalArgumentException if percentage is not between 0 and 100.
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
     * Sets up the Alarm Volume to its maximum value (in percentage).
     * @param context The current Context
     */
    public static void setMaxAlarmVolume(Context context) {
        setAlarmVolume(context, MAX_PERCENTAGE);
    }

    /**
     * Sets up the Alarm Volume to its minimum value (in percentage).
     * @param context The current Context
     */
    public static void setMinAlarmVolume(Context context) {
        setAlarmVolume(context, MIN_PERCENTAGE);
    }

    /**
     * @param context The current Context
     * @return The current Ringtone Volume (in percentage).
     */
    public static int getCurrentRingtoneVolume(Context context) {
        int currentVolume = getAudioManager(context).getStreamVolume(AudioManager.STREAM_RING);

        int maxVolume = getMaxRingtoneVolume(context);
        return Math.round(MAX_PERCENTAGE * currentVolume / maxVolume);
    }

    /**
     * @param context The current Context
     * @return The maximum Ringtone volume (real value).
     */
    private static int getMaxRingtoneVolume(Context context) {
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_RING);
    }

    /**
     * Sets up the Ringtone Volume, given a certain percentage.
     * @param context The current Context
     * @param percentage Target volume (expressed in %).
     * @throws IllegalArgumentException if percentage is not between 0 and 100.
     */
    public static void setRingtoneVolume(Context context, int percentage) throws IllegalArgumentException {
        if (percentage < MIN_PERCENTAGE)
            throw new IllegalArgumentException(" Your value is too low. Please insert a value between 0 and 100.");
        else if (percentage > MAX_PERCENTAGE) {
            throw new IllegalArgumentException(" Your value is too high. Please insert a value between 0 and 100.");
        }

        int maxVolume = getMaxRingtoneVolume(context);

        // Calculate the real value of the new volume
        int newVolume = maxVolume * percentage;
        newVolume = Math.round(newVolume / MAX_PERCENTAGE);

        // Set the Ringtone volume
        getAudioManager(context).setStreamVolume(
                AudioManager.STREAM_RING,
                newVolume,
                AudioManager.FLAG_SHOW_UI
        );
    }

    /**
     * Sets up the Ringtone Volume to its maximum value (in percentage).
     * @param context The current Context
     */
    public static void setMaxRingtoneVolume(Context context) {
        setRingtoneVolume(context, MAX_PERCENTAGE);
    }

    /**
     * Sets up the Ringtone Volume to its minimum value (in percentage).
     * @param context The current Context
     */
    public static void setMinRingtoneVolume(Context context) {
        setRingtoneVolume(context, MIN_PERCENTAGE);
    }

    /**
     * @param context The current Context
     * @return The current Music Volume (in percentage).
     */
    public static int getCurrentMusicVolume(Context context) {
        int currentVolume = getAudioManager(context).getStreamVolume(AudioManager.STREAM_MUSIC);

        int maxVolume = getMaxMusicVolume(context);
        return Math.round(MAX_PERCENTAGE * currentVolume / maxVolume);
    }

    /**
     * @param context The current Context
     * @return The maximum Music volume (real value).
     */
    private static int getMaxMusicVolume(Context context) {
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * Sets up the Music Volume, given a certain percentage.
     * @param context The current Context
     * @param percentage Target volume (expressed in %).
     * @throws IllegalArgumentException if percentage is not between 0 and 100.
     */
    public static void setMusicVolume(Context context, int percentage) throws IllegalArgumentException {
        if (percentage < MIN_PERCENTAGE)
            throw new IllegalArgumentException(" Your value is too low. Please insert a value between 0 and 100.");
        else if (percentage > MAX_PERCENTAGE) {
            throw new IllegalArgumentException(" Your value is too high. Please insert a value between 0 and 100.");
        }

        int maxVolume = getMaxMusicVolume(context);

        // Calculate the real value of the new volume
        int newVolume = maxVolume * percentage;
        newVolume = Math.round(newVolume / MAX_PERCENTAGE);

        // Set the Music volume
        getAudioManager(context).setStreamVolume(
                AudioManager.STREAM_MUSIC,
                newVolume,
                AudioManager.FLAG_SHOW_UI
        );
    }

    /**
     * Sets up the Music Volume to its maximum value (in percentage).
     * @param context The current Context
     */
    public static void setMaxMusicVolume(Context context) {
        setMusicVolume(context, MAX_PERCENTAGE);
    }

    /**
     * Sets up the Music Volume to its minimum value (in percentage).
     * @param context The current Context
     */
    public static void setMinMusicVolume(Context context) {
        setMusicVolume(context, MIN_PERCENTAGE);
    }

    /**
     * Sets minimum Volume for Alarm, Ringtone and Music Streams.
     * @param context The current Context
     */
    public static void setQuietMode(Context context){
        setMinAlarmVolume(context);
        setMinRingtoneVolume(context);
        setMinMusicVolume(context);
    }

    /**
     * Sets maximum Volume for Alarm, Ringtone and Media Streams.
     * @param context The current Context.
     */
    public static void setLoudMode(Context context){
        setMaxAlarmVolume(context);
        setMaxRingtoneVolume(context);
        setMaxMusicVolume(context);
    }

}