package audioUtility;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


public class AudioUtilityManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = this;

        // Get the audio manager instance
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        //Setup the volume.
        setupVolumeManager();
    }

    //private static final int AUDIO_SETTINGS_PERMISSION_CODE = 1;

    private Context mContext;
    private Activity mActivity;

    // Attualmente non so come usarlo. Intanto mi occupo dei permessi, poi ci si occuperà
    // della View.
    private RecyclerView listView;


    private AudioManager mAudioManager;

    /*
    private boolean checkVolumePermission() {
        return checkSelfPermission(this,AudioManager.);

    }
    */

    private void setupVolumeManager() {
        // If do not disturb mode on, then off it first
        // turnOffDoNotDisturbMode();
        // NON FUNZIONA. SERVE METODO ALTERNATIVO PER BYPASSARE IL DND.

        /*
        // Get the ringer current volume level
        int current_volume_level = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
         */

        // Get the ringer maximum volume
        int max_volume_level = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

        // calculate approx. 50% of the volume
        int half_volume = max_volume_level/2;
        // Set the ringer volume
        mAudioManager.setStreamVolume(
                AudioManager.STREAM_RING,
                half_volume,
                AudioManager.FLAG_SHOW_UI
        );
        Toast.makeText(this, "RINGTONE AUDIO IS SET TO 50%.", Toast.LENGTH_SHORT).show();
//        Context ctx = getApplicationContext();
        ;
        /*
        if (!AudioManager.getInstance(ctx).isSetup()) {
            AudioManager.getInstance(ctx).setup(APP_ID);
        }
        AudioManager.getInstance(ctx).addReceivedMessageListener(this);
        */
    }

}
