package com.gruppo4.SMSApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;
import com.gruppo4.sms.SMSSendListener;

/**
 * @author Tommasini Marco
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permissione denied to SEND_SMS - requesting it");
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
            }
            if(checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permissione denied to RECEIVE_SMS - requesting it");
                requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnSend = findViewById(R.id.btnSend);
        final EditText edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        final ImageView imgvSend = findViewById(R.id.imgvSend);

        //SMSController handles every events for a message
        //Change color of ImageView
        final SMSController controller = new SMSController();
        controller.listener = new SMSSendListener() {
            @Override
            public void onSent(SMSMessage message, SMSController.SMSState state) {
                imgvSend.setColorFilter(ActivityCompat.getColor(getApplicationContext(), android.R.color.holo_orange_light));
            }

            @Override
            public void onDelivered(SMSMessage message, SMSController.SMSState state) {
                imgvSend.setColorFilter(ActivityCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark));
            }

            @Override
            public void onReceived(SMSMessage message) {
                Toast.makeText(getApplicationContext(), message.getMsgBody() + " from " + message.getDestinationAddress(), Toast.LENGTH_SHORT).show();
            }
        };

        //Try to send message if not wrong
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgvSend.setColorFilter(ActivityCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark));

                SMSMessage sms = new SMSMessage(getResources().getString(R.string.ding), edtPhoneNumber.getText().toString());
                if(sms.getMsgBody() == null && sms.getDestinationAddress() == null)
                    Toast.makeText(MainActivity.this, "Il numero " + edtPhoneNumber.getText().toString() + " è errato", Toast.LENGTH_SHORT).show();
                else controller.sendMessage(view.getContext(), sms);
            }
        });





    }
}
