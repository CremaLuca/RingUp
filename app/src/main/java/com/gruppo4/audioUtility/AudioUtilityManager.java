package com.gruppo4.audioUtility;


import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import static android.content.Context.AUDIO_SERVICE;


public class AudioUtilityManager {

    /**
     *
     * @param context
     * @return The current volume.
     */
    public static int getCurrentVolume(Context context){
        // Get the ringer current volume level
        int currentVolume = getAudioManager(context).getStreamVolume(AudioManager.STREAM_RING);
        // Get the ringer maximum volume
        int maxVolume = getMaxVolume(context);
        return Math.round(100*currentVolume/maxVolume);
    }

    /**
     *
     * @param context
     * @return The maximum volume.
     */
    public static int getMaxVolume(Context context){
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
     * @param context
     * Sets up the Ringtone Volume a certain percentage.
     */
    public static void setRingtoneVolume(Context context, int percentage) {
        // If do not disturb mode on, then off it first
        // turnOffDoNotDisturbMode();
        // NON FUNZIONA. SERVE METODO ALTERNATIVO PER BYPASSARE IL DND.

        if(percentage<0)
            Log.d("AudioUtilityManager",percentage+" %. A percentage cannot be below 0%. Considering its absolute value");
        else if(percentage>100) {
            Log.d("AudioUtilityManager", +percentage+" %. This percentage is too high. Considering 100%.");
            percentage = 100;
        }
        percentage = Math.abs(percentage);
        // Get the ringer maximum volume
        int maxVolume = getMaxVolume(context);

        // calculate the new volume
        int newVolume = maxVolume*percentage;
        newVolume = Math.round(newVolume/100);

        // Set the ringer volume
        getAudioManager(context).setStreamVolume(
                AudioManager.STREAM_RING,
                newVolume,
                AudioManager.FLAG_SHOW_UI
        );

    }

}
