package com.gruppo4.RingApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gruppo4.RingApplication.ringCommands.PasswordManager;
import com.gruppo4.RingApplication.ringCommands.dialog.PasswordDialog;
import com.gruppo4.RingApplication.ringCommands.dialog.PasswordDialogListener;
import com.gruppo4.RingApplication.ringCommands.exceptions.IllegalCommandException;
import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo_4.preferences.PreferencesManager;

import static com.gruppo4.RingApplication.MainActivity.CHANGE_PASS_COMMAND;
import static java.lang.Integer.parseInt;

/**
 * @author Alberto Ursino
 */
public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PasswordDialogListener {

    private static final int WAIT_TIME = 2000;
    private static final int PERMISSION_CODE = 0;
    private Spinner TIMER_SPINNER = null;
    private final static String TIMER_STRING_KEY = "Timer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Context context = getApplicationContext();

        TIMER_SPINNER = findViewById(R.id.timer_selection);
        final Button CHANGE_PASSWORD_BUTTON = findViewById(R.id.change_password);
        final RadioButton RINGTONE_RADIO_BUTTON = findViewById(R.id.ringtone_mode);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(context, R.array.seconds, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TIMER_SPINNER.setAdapter(arrayAdapter);
        TIMER_SPINNER.setOnItemSelectedListener(this);

        /**
         * If a value of timer is actually stored in memory then updates it
         */
        if(!(PreferencesManager.getInt(context, TIMER_STRING_KEY) == PreferencesManager.DEFAULT_INTEGER_RETURN)) {
            //TODO
        }

        RINGTONE_RADIO_BUTTON.toggle();

        //Button used to open a dialog where the user can change the password
        CHANGE_PASSWORD_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }

    /**
     * Before returning to the mainActivity we save the value set on timer
     */
    @Override
    protected void onPause() {
        saveTimer((String) TIMER_SPINNER.getSelectedItem());
        Log.d("saveTimer, timer val: ", "" + TIMER_SPINNER.getSelectedItem());
        super.onPause();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void applyText(String password, Context context) {
        new PasswordManager(context).setPassword(password);
        Toast.makeText(getApplicationContext(), "Password saved", Toast.LENGTH_SHORT).show();
        waitForPermissions(WAIT_TIME);
    }

    /**
     * Creates the dialog used to insert a valid password or exit/abort
     *
     * @throws IllegalCommandException
     */
    void openDialog() {
        if (PasswordDialog.isCommandChangePass(CHANGE_PASS_COMMAND)) {
            PasswordDialog passwordDialog = new PasswordDialog(CHANGE_PASS_COMMAND);
            passwordDialog.show(getSupportFragmentManager(), "Change Password");
        } else {
            throw new IllegalCommandException();
        }
    }

    /**
     * @param time to wait before checking permits
     */
    void waitForPermissions(int time) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermission(getApplicationContext());
            }
        }, time);
    }

    /**
     * Simple method used to check permissions
     *
     * @param context of the application
     */
    void checkPermission(Context context) {
        if (!SMSHandler.checkReceivePermission(context))
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSION_CODE);
    }

    /**
     * @param timer value taken from the list of timer values
     */
    public void saveTimer(String timer) {
        PreferencesManager.setInt(getApplicationContext(), "Timer", parseInt(timer, 10));
    }
}
