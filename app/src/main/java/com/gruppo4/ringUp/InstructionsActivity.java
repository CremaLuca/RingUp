package com.gruppo4.ringUp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class used to inform the user on what ringUp is and what it is used for.
 * It also requires the setting up of the device password
 *
 * @author Francesco Bau'
 * @author Alberto Ursino
 * @since 07/01/2020
 */
public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
    }

}
