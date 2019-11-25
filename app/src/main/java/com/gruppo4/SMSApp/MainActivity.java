package com.gruppo4.SMSApp;

/**
 * The MainActivity's scope is to test AudioUtilityManager's methods.
 *
 * @author Francesco Bau'
 */

import com.gruppo4.audioUtility.AudioUtilityManager;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    // Default app volume, expressed in percentage
    private static final int DEFAULT_APP_VOLUME = 50;
    // The selected Stream.
    private static AudioUtilityManager.AUMStream stream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Default choice: Stream ALARM
        stream = AudioUtilityManager.AUMStream.ALARM;

        // DEFAULT VOLUME Button
        View defaultVolumeButton = findViewById(R.id.button_defaultVolume);
        defaultVolumeButton.setOnClickListener(getTestListener(defaultVolumeButton));

        // MAXIMUM VOLUME Button
        View maximumVolumeButton = findViewById(R.id.button_maxVolume);
        maximumVolumeButton.setOnClickListener(getTestListener(maximumVolumeButton));

        // MINIMUM VOLUME Button
        View minimumVolumeButton = findViewById(R.id.button_minVolume);
        minimumVolumeButton.setOnClickListener(getTestListener(minimumVolumeButton));

        // SINGLE VIBRATE Button
        View singleVibrateButton = findViewById(R.id.button_singleVibrate);
        singleVibrateButton.setOnClickListener(getTestListener(singleVibrateButton));

        // MULTIPLE VIBRATE Button
        View multipleVibrateButton = findViewById(R.id.button_multipleVibrate);
        multipleVibrateButton.setOnClickListener(getTestListener(multipleVibrateButton));

        // STOP VIBRATE Button
        View stopVibrateButton = findViewById(R.id.button_stopVibrate);
        stopVibrateButton.setOnClickListener(getTestListener(stopVibrateButton));

        // ALARM STREAM Button
        View alarmStreamButton = findViewById(R.id.alarmButton);
        alarmStreamButton.setOnClickListener(getStreamListener(alarmStreamButton));

        // MUSIC STREAM Button
        View musicStreamButton = findViewById(R.id.musicButton);
        musicStreamButton.setOnClickListener(getStreamListener(musicStreamButton));

        // RING STREAM Button
        View ringStreamButton = findViewById(R.id.ringButton);
        ringStreamButton.setOnClickListener(getStreamListener(ringStreamButton));

        // VIBRATE ONLY MODE Button
        View vibrateOnlyModeButton = findViewById(R.id.vibrateButton);
        vibrateOnlyModeButton.setOnClickListener(getStreamListener(vibrateOnlyModeButton));

    }

    /**
     * @param view The button (View) from R (Resource) package. Must be one of the expected test buttons from activity_main.
     * @return The OnClickListener of the relative button
     * @throws IllegalArgumentException if parameter is not valid.
     */
    private View.OnClickListener getTestListener(View view) throws IllegalArgumentException {
        switch (view.getId()) {
            // Action performed by clicking button_defaultVolume
            // -> Sets DEFAULT Volume for the current Stream
            // -> TEST the method getVolume(Context,String).
            // -> TEST the method setVolume(Context,String,int).
            case R.id.button_defaultVolume:
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Sets up the volume at default percentage.
                        AudioUtilityManager.setVolume(MainActivity.this, stream, DEFAULT_APP_VOLUME);
                        int defaultVolume = AudioUtilityManager.getVolume(MainActivity.this, stream);
                        Toast.makeText(MainActivity.this, "DEFAULT " + stream + " VOLUME: " + defaultVolume + " %.", Toast.LENGTH_SHORT).show();
                    }
                };
            // Action performed by clicking button_maxVolume
            // -> Sets MAXIMUM Volume for the current Stream
            // -> TEST the method setVolumeToMax(Context,String).
            case R.id.button_maxVolume:
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AudioUtilityManager.setVolumeToMax(MainActivity.this, stream);
                        int maxVolume = AudioUtilityManager.getVolume(MainActivity.this, stream);
                        Toast.makeText(MainActivity.this, "MAXIMUM " + stream + " VOLUME: " + maxVolume + " %.", Toast.LENGTH_SHORT).show();
                    }
                };
            // Action performed by clicking button_minVolume
            // -> Sets MINIMUM Volume for the current Stream.
            // -> TEST the method setVolumeToMin(Context,String).
            case R.id.button_minVolume:
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AudioUtilityManager.setVolumeToMin(MainActivity.this, stream);
                        int minVolume = AudioUtilityManager.getVolume(MainActivity.this, stream);
                        Toast.makeText(MainActivity.this, "MINIMUM " + stream + " VOLUME: " + minVolume + " %.", Toast.LENGTH_SHORT).show();
                    }
                };
            // Action performed by clicking button_singleVibrate
            // -> SINGLE Vibration
            // -> TEST the "Vibrator". (One-shot)
            case R.id.button_singleVibrate:
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AudioUtilityManager.singleVibrate(MainActivity.this);
                        Toast.makeText(MainActivity.this, "SINGLE VIBRATION IS IN PROGRESS...", Toast.LENGTH_SHORT).show();
                    }
                };
            // Action performed by clicking button_multipleVibrate
            // -> MULTIPLE Vibration
            // -> TEST the "Vibrator" (Multiple)
            case R.id.button_multipleVibrate:
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AudioUtilityManager.multipleVibrate(MainActivity.this);
                        Toast.makeText(MainActivity.this, "MULTIPLE VIBRATION IS IN PROGRESS...", Toast.LENGTH_SHORT).show();
                    }
                };
            // Action performad by clicking button_stopVibrate
            // -> STOP Vibration.
            case R.id.button_stopVibrate:
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AudioUtilityManager.stopVibrate(MainActivity.this);
                        Toast.makeText(MainActivity.this, "STOPPING VIBRATION...", Toast.LENGTH_SHORT).show();
                    }
                };
            default:
                throw new IllegalArgumentException("Unexpected View as parameter.");
        }
    }

    /**
     * @param view The button (View) from R (Resource) package. Must be one of the expected Stream buttons from activity_main.
     * @return The OnClickListener of the relative button
     * @throws IllegalArgumentException if parameter is not valid.
     */
    private View.OnClickListener getStreamListener(View view) throws IllegalArgumentException {
        switch (view.getId()) {
            // Action performed by clicking musicButton
            // -> Select MUSIC Stream
            case R.id.musicButton:
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stream = AudioUtilityManager.AUMStream.MUSIC;
                    }
                };
            // Action performed by clicking ringButton
            // -> Select RING Stream
            case R.id.ringButton:
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stream = AudioUtilityManager.AUMStream.RING;
                    }
                };
            // Action performed by clicking vibrateButton
            // -> Turn on VIBRATE Mode
            case R.id.vibrateButton:
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        Toast.makeText(MainActivity.this, "ENABLED ONLY-VIBRATE MODE", Toast.LENGTH_SHORT).show();
                    }
                };
            // Default Stream button -> alarmButton
            // WARNING! This might not be a good idea... It's just to make it default
            // -> SELECT ALARM Stream if clicked
            default:
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stream = AudioUtilityManager.AUMStream.ALARM;
                        Toast.makeText(MainActivity.this, "Default Stream: "+AudioUtilityManager.AUMStream.ALARM, Toast.LENGTH_SHORT).show();
                    }
                };
        }
    }

}