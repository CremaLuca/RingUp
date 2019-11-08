package com.gruppo4.audioUtility;

/**
 * @author Francesco Bau'
 */

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import static android.content.Context.AUDIO_SERVICE;


public class AudioUtilityManager {

    // Maximum percentage is 100% , minimum percentage is 0%.
    public static final int MAX_PERCENTAGE = 100;
    public static final int MIN_PERCENTAGE = 0;

    /**
     *
     * @param context
     * @return The current Ringtone Volume.
     */
    public static int getCurrentRingtoneVolume(Context context){
        // Get the ringer current volume level
        int currentVolume = getAudioManager(context).getStreamVolume(AudioManager.STREAM_RING);
        // Get the ringer maximum volume
        int maxVolume = getMaxRingtoneVolume(context);
        return Math.round(MAX_PERCENTAGE*currentVolume/maxVolume);
    }

    /**
     *
     * @param context
     * @return The maximum Ringtone volume.
     */
    public static int getMaxRingtoneVolume(Context context){
        // Get the ringer maximum volume
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_RING);
    }

    /**
     *
     * @param context
     * @return The current AudioManager, determined by a certain Context.
     */
    public static AudioManager getAudioManager(Context context){
        // Get the audio manager instance
        return (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    /**
     *
     * @param context,percentage
     * Sets up the Ringtone Volume a certain percentage.
     */
    public static void setRingtoneVolume(Context context, int percentage) {
        // If do not disturb mode on, then off it first
        // turnOffDoNotDisturbMode();
        // NON FUNZIONA. SERVE METODO ALTERNATIVO PER BYPASSARE IL DND.

        if(percentage<MIN_PERCENTAGE)
            Log.d("AudioUtilityManager",percentage+" %. A percentage cannot be below 0%. Considering its absolute value");
        else if(percentage>MAX_PERCENTAGE) {
            Log.d("AudioUtilityManager", +percentage+" %. This percentage is too high. Considering 100%.");
            percentage = MAX_PERCENTAGE;
        }
        percentage = Math.abs(percentage);
        // Get the ringer maximum volume
        int maxVolume = getMaxRingtoneVolume(context);

        // calculate the new volume
        int newVolume = maxVolume*percentage;
        newVolume = Math.round(newVolume/MAX_PERCENTAGE);

        // Set the ringer volume
        getAudioManager(context).setStreamVolume(
                AudioManager.STREAM_RING,
                newVolume,
                AudioManager.FLAG_SHOW_UI
        );

    }
    /**
     * @param context
     * Sets up the Ringtone Volume to its maximum value.
     */
    public static void setMaxRingtoneVolume(Context context){
        setRingtoneVolume(context, MAX_PERCENTAGE);
    }

}
