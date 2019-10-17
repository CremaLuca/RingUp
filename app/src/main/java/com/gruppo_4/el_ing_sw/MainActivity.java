package com.gruppo_4.el_ing_sw;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import com.gruppo_4.sms_library.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Richiediamo il permesso di leggere i messaggi
        requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},1);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
