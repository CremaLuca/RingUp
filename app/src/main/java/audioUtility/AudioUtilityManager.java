package audioUtility;


import android.content.Context;
import android.media.AudioManager;
import android.widget.Toast;
import static android.content.Context.AUDIO_SERVICE;


public class AudioUtilityManager {

    /**
     *
     * @param context
     * Method to get the current volume, shown on a Toast.
     */
    public static void getCurrentVolume(Context context){
        // Get the ringer current volume level
        int current_volume_level = getAudioManager(context).getStreamVolume(AudioManager.STREAM_RING);

        // Show it on the Toast
        Toast.makeText(context, "CURRENT RINGTONE VOLUME: "+current_volume_level, Toast.LENGTH_SHORT).show();
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
     * Sets up the Ringtone Volume at 50%, and shows a Toast, telling the user about the change of that setting.
     */
    public static void setupVolumeManager(Context context) {
        // If do not disturb mode on, then off it first
        // turnOffDoNotDisturbMode();
        // NON FUNZIONA. SERVE METODO ALTERNATIVO PER BYPASSARE IL DND.

        // Get the ringer maximum volume
        int max_volume_level = getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_RING);

        // calculate approx. 50% of the volume
        int half_volume = max_volume_level/2;
        // Set the ringer volume
        getAudioManager(context).setStreamVolume(
                AudioManager.STREAM_RING,
                half_volume,
                AudioManager.FLAG_SHOW_UI
        );
        Toast.makeText(context, "RINGTONE AUDIO IS SET TO 50%.", Toast.LENGTH_SHORT).show();

    }

}
