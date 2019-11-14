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

    public static final String ALARM = "ALARM";
    public static final String RING = "RING";
    public static final String MUSIC = "MUSIC";

    /**
     * @param context The current Context
     * @return The current AudioManager instance.
     */
    protected static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    /**
     * @param context The current Context
     * @param stream The chosen Stream (should be"ALARM"/"RING"/"MUSIC", otherwise an exception is thrown)
     * @return The current Stream Volume (in percentage).
     * @throws IllegalArgumentException if parameter Stream is illegal
     */
    public static int getVolume(Context context, String stream) throws IllegalArgumentException {
        int currentVolume;
        switch (stream){
            case ALARM: {
                currentVolume = getAudioManager(context).getStreamVolume(AudioManager.STREAM_ALARM);
                break;
            }
            case RING:{
                currentVolume = getAudioManager(context).getStreamVolume(AudioManager.STREAM_RING);
                break;
            }
            case MUSIC:{
                currentVolume = getAudioManager(context).getStreamVolume(AudioManager.STREAM_MUSIC);
                break;
            }
            default: throw new IllegalArgumentException("\nIllegal choice of Stream. Choose between \"ALARM\"," +
                    " \"RING\" or \"MUSIC\" next time.");
        }

        int maxVolume = getMaxVolume(context,stream);
        return Math.round(MAX_PERCENTAGE * currentVolume / maxVolume);
    }

    /**
     * @param context The current Context
     * @param stream The chosen Stream (should be"ALARM"/"RING"/"MUSIC", otherwise an exception is thrown)
     * @return The maximum Stream volume (real value).
     * @throws IllegalArgumentException if parameter Stream is illegal
     */
    private static int getMaxVolume(Context context, String stream) throws IllegalArgumentException {
        String streamUP = stream.toUpperCase();
        switch (streamUP){
            case ALARM: {
                return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_ALARM);
            }
            case RING:{
                return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_RING);
            }
            case MUSIC:{
                return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            }
            default: throw new IllegalArgumentException("\nIllegal choice of Stream. Choose between \"ALARM\"," +
                    " \"RING\" or \"MUSIC\" next time.");
        }
    }

    /**
     * Sets up the Stream Volume, given a certain percentage.
     * @param context The current Context
     * @param stream The chosen Stream (should be"ALARM"/"RING"/"MUSIC", otherwise an exception is thrown)
     * @param percentage Target volume (expressed in %).
     * @throws IllegalArgumentException if percentage is not between 0 and 100.
     * @throws IllegalArgumentException if parameter Stream is illegal.
     */
    public static void setVolume(Context context, String stream, int percentage) throws IllegalArgumentException {
        if (percentage < MIN_PERCENTAGE)
            throw new IllegalArgumentException(" Your value is too low. Please insert a value between 0 and 100.");
        else if (percentage > MAX_PERCENTAGE) {
            throw new IllegalArgumentException(" Your value is too high. Please insert a value between 0 and 100.");
        }
        int maxVolume = getMaxVolume(context,stream);

        // Calculate the real value of the new volume
        int newVolume = maxVolume * percentage;
        newVolume = Math.round(newVolume / MAX_PERCENTAGE);
        switch (stream){
            case ALARM: {
                // Set the Alarm volume
                getAudioManager(context).setStreamVolume(
                        AudioManager.STREAM_ALARM,
                        newVolume,
                        AudioManager.FLAG_SHOW_UI
                );
                break;
            }
            case RING:{
                // Set the Ring volume
                getAudioManager(context).setStreamVolume(
                        AudioManager.STREAM_RING,
                        newVolume,
                        AudioManager.FLAG_SHOW_UI
                );
                break;
            }
            case MUSIC:{
                // Set the Music volume
                getAudioManager(context).setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        newVolume,
                        AudioManager.FLAG_SHOW_UI
                );
                break;
            }
            default: throw new IllegalArgumentException("\nIllegal choice of Stream. Choose between \"ALARM\"," +
                    " \"RING\" or \"MUSIC\" next time.");
        }


    }

    /**
     * Sets up the Stream Volume to its maximum value (in percentage).
     * @param context The current Context
     * @param stream The chosen Stream (should be"ALARM"/"RING"/"MUSIC", otherwise an exception is thrown)
     * @throws IllegalArgumentException if parameter Stream is illegal
     */
    public static void setMaxVolume(Context context, String stream) throws IllegalArgumentException {
        String streamUP = stream.toUpperCase();
        if(!(streamUP.equals(ALARM)||streamUP.equals(RING)||streamUP.equals(MUSIC)))
            throw new IllegalArgumentException("\nIllegal choice of Stream. Choose between \"ALARM\"," +
                    " \"RING\" or \"MUSIC\" next time.");
        else setVolume(context,stream,MAX_PERCENTAGE);
    }

    /**
     * Sets up the Stream Volume to its minimum value (in percentage).
     * @param context The current Context
     * @param stream The chosen Stream (should be"ALARM"/"RING"/"MUSIC", otherwise an exception is thrown)
     * @throws IllegalArgumentException if parameter Stream is illegal
     */
    public static void setMinVolume(Context context, String stream) throws IllegalArgumentException{
        String streamUP = stream.toUpperCase();
        if(!(streamUP.equals(ALARM)||streamUP.equals(RING)||streamUP.equals(MUSIC)))
            throw new IllegalArgumentException("\nIllegal choice of Stream. Choose between \"ALARM\"," +
                    " \"RING\" or \"MUSIC\" next time.");
        else setVolume(context,stream,MIN_PERCENTAGE);
    }

    /**
     * Sets minimum Volume for Alarm, Ringtone and Music Streams.
     * @param context The current Context
     * @throws IllegalArgumentException if parameter Stream is illegal
     */
    public static void setQuietMode(Context context)throws IllegalArgumentException{
        setMinVolume(context,ALARM);
        setMinVolume(context,MUSIC);
        setMinVolume(context,RING);
    }

    /**
     * Sets maximum Volume for Alarm, Ringtone and Media Streams.
     * @param context The current Context.
     * @throws IllegalArgumentException if parameter Stream is illegal
     */
    public static void setLoudMode(Context context) throws IllegalArgumentException{
        setMaxVolume(context,ALARM);
        setMaxVolume(context,MUSIC);
        setMaxVolume(context,RING);
    }

    /**
     * Sets the Volume for Alarm, Ringtone and Media Streams.
     * @param context The current Context.
     * @param percentage Target volume (expressed in %).
     * @throws IllegalArgumentException if parameter Stream is illegal
     */
    public static void setAllVolumes(Context context,int percentage) throws IllegalArgumentException{
        setVolume(context,ALARM,percentage);
        setVolume(context,MUSIC,percentage);
        setVolume(context,RING,percentage);
    }

}
//TODO TESTING