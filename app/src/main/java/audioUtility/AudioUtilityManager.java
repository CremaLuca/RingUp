package audioUtility;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.gruppo4.SMSApp.R;


public class AudioUtilityManager extends AppCompatActivity {


    //private static final int AUDIO_SETTINGS_PERMISSION_CODE = 1;

    private Context mContext;
    private Activity mActivity;

    // Attualmente non so come usarlo. Intanto mi occupo dei permessi, poi ci si occuperà
    // della View.
    private RecyclerView listView;


    private NotificationManager mNotificationManager;

    //Meglio non mettere static, altrimenti crasha
    //private AudioManager mAudioManager;


    private TextView mTextView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = this;

        // Connect RecyclerView, Button and TextView
        listView = findViewById(R.id.recyclerView);
        mTextView = findViewById(R.id.hello_world);
        mButton = findViewById(R.id.current_volume_button);

        // Listener for the button
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HARDWIRING...
                onButtonClick(getApplicationContext(), 1);
            }
        });

        // Get the notification manager instance
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        //Setup the volume.
        setupVolumeManager(this);
    }

    private void onButtonClick(Context applicationContext, int internalCode) {

        // Get the audio manager instance
        AudioManager mAudioManager = getAudioManager(applicationContext);

        // Get the ringer current volume level
        int current_volume_level = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);

        // Show it on the Toast
        Toast.makeText(applicationContext, "CURRENT RINGTONE VOLUME: "+current_volume_level, Toast.LENGTH_SHORT).show();

        // Show it on the TextView (experimental, and I don't know how to do it... Should I use the Adapter?

    }


    /*
    private boolean checkVolumePermission() {
        return checkSelfPermission(this,AudioManager.);

    }
    */
    public static AudioManager getAudioManager(Context context){
        // Get the audio manager instance
        return (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

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
