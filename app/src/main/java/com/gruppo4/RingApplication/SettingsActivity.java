package com.gruppo4.RingApplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gruppo4.RingApplication.structure.PasswordManager;
import com.gruppo4.RingApplication.structure.dialog.PasswordDialog;
import com.gruppo4.RingApplication.structure.dialog.PasswordDialogListener;
import com.gruppo4.RingApplication.structure.exceptions.IllegalCommandException;

import it.lucacrema.preferences.PreferencesManager;

import static com.gruppo4.RingApplication.MainActivity.CHANGE_PASS_COMMAND;

/**
 * @author Alberto Ursino
 */
public class SettingsActivity extends AppCompatActivity implements PasswordDialogListener {

    public static final int DEFAULT_TIMER_VALUE = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setSupportActionBar(findViewById(R.id.actionBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupTimeSpinner();

        FloatingActionButton changePasswordButton = findViewById(R.id.change_password_button);

        //Button used to open a dialog where the user can change the password
        changePasswordButton.setOnClickListener(v -> openDialog());

    }

    /**
     * Sets up the timer spinner by populating it and set default to previous selection
     * Also adds the callback for spinner value changed
     */
    private void setupTimeSpinner() {
        Spinner spinner = findViewById(R.id.timeout_time_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.timer_names, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Set the previous selected item
        spinner.setSelection(findCurrentTimeoutSpinnerIndex());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("SettingsActivity", "Selected " + position + "^ element on timer spinner");
                TypedArray timerValues = getResources().obtainTypedArray(R.array.timer_values);
                int selectedTime = timerValues.getInt(position, DEFAULT_TIMER_VALUE);
                Log.d("SettingsActivity", "Selected timer value " + selectedTime);
                //Save the value in memory
                PreferencesManager.setInt(getApplicationContext(), MainActivity.TIMEOUT_TIME_PREFERENCES_KEY, selectedTime);
                timerValues.recycle(); //So that timer values can be used again (required)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Nothing to do, nothing to update
                Log.v("SettingsActivity", "Nothing selected in timer spinner");
            }
        });
    }

    /**
     * Looks into preferences and finds the current timeout time, checks every timer_values resources array and
     * finds the index of the current timeout time
     *
     * @return
     */
    private int findCurrentTimeoutSpinnerIndex() {
        int[] resourcesTimerValues = getResources().getIntArray(R.array.timer_values);
        int currentTimeoutTime = PreferencesManager.getInt(getApplicationContext(), MainActivity.TIMEOUT_TIME_PREFERENCES_KEY);
        for (int i = 0; i < resourcesTimerValues.length; i++) {
            if (currentTimeoutTime == resourcesTimerValues[i])
                return i;
        }
        return 0;
    }

    @Override
    public void onPasswordSet(String password, Context context) {
        new PasswordManager(context).setPassword(password);
        Toast.makeText(getApplicationContext(), "Password saved", Toast.LENGTH_SHORT).show();
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

}
