package com.gruppo4.SMSApp;

/**
 *@author Francesco Bau'
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.audioUtility.AudioUtilityManager;


public class MainActivity extends AppCompatActivity {

    //Default ringtone volume (%) for the app
    private static final int DEFAULT_APP_VOLUME = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Sets up the volume at a certain percentage.
        AudioUtilityManager.setRingtoneVolume(this, DEFAULT_APP_VOLUME);

        // TESTING the method getCurrentRingtoneVolume(Context)
        int currentVolume = AudioUtilityManager.getCurrentRingtoneVolume(this);
        Toast.makeText(this, "CURRENT RINGTONE VOLUME: "+currentVolume+" %.", Toast.LENGTH_SHORT).show();


        // TESTING the method setMaxRingtoneVolume(Context), after waiting 5 seconds.
        Fred test1 = new Fred(this,1);
        test1.start();
        try {
            test1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, test1.getMessage(), Toast.LENGTH_SHORT).show();


        // TESTING the method setMinRingtoneVolume(Context), after waiting 5 seconds.
        Fred test2 = new Fred(this,2);
        try {
            //test2.wait();
            test2.start();
            test2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, test2.getMessage(), Toast.LENGTH_SHORT).show();



    }

    private class Fred extends Thread{
        private Context context;
        private String tag,message;
        private int code;
        public Fred(Context context, int code){
            setFred(context,code);
        }
        private void setFred(Context context, int code) throws IllegalStateException{
            this.context = context;
            this.code = code;
            this.tag = "[Test"+code+"]";
        }
        public String getTag(){
            return this.tag;
        }
        public String getMessage(){
            return this.message;
        }
        public int getCode(){
            return this.code;
        }

        @Override
        public void run() throws IllegalArgumentException{
            super.run();
            Log.d(getTag(),"WAITING 5 SECONDS...");
            try {
                // HARDWIRING...
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(getTag(),"RESUMING...");
            switch (code){
                case 1:{
                    AudioUtilityManager.setMaxRingtoneVolume(this.context);
                    int maxVolume = AudioUtilityManager.getCurrentRingtoneVolume(this.context);
                    message = "MAXIMUM RINGTONE VOLUME: "+maxVolume+" %.";
                    break;
                }
                case 2:{
                    AudioUtilityManager.setMinRingtoneVolume(this.context);
                    int minVolume = AudioUtilityManager.getCurrentRingtoneVolume(this.context);
                    message = "MINIMUM RINGTONE VOLUME: "+minVolume+" %.";
                    break;
                }
                default:{
                    throw new IllegalArgumentException(" ERROR. The entered code is NOT valid. Please retry with a valid code.");
                }
            }
            // Toasts create conflicts with Thread (Handler)
            //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.d(getTag(),getMessage());
        }
    }
}
//TODO TESTING