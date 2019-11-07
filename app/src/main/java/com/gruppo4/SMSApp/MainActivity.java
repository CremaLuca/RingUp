package com.gruppo4.SMSApp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    PasswordManager passwordManager = new PasswordManager();
    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView2);
        editText = (EditText) findViewById(R.id.editText);

        Button save = (Button) findViewById(R.id.button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editText.getText().toString();
                if (password.equals(""))
                    Toast.makeText(getApplicationContext(), "Empty password, retry...", Toast.LENGTH_SHORT).show();
                else
                    setPass(password);
            }
        });

        Button load = (Button) findViewById(R.id.button2);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPass();
            }
        });
    }

    public void setPass(String password) {
        passwordManager.setPassword(getApplicationContext(), password);
        Toast.makeText(getApplicationContext(), "Password saved, now click on the button below", Toast.LENGTH_SHORT).show();
    }

    public void showPass() {
        textView.setText(passwordManager.getPassword(getApplicationContext()));
    }
}
