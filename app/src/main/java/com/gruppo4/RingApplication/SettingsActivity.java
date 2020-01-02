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
 * @author Luca Crema
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
        changePasswordButton.setOnClickListener(v -> openDialog(CHANGE_PASS_COMMAND));

    }

    /**
     * Sets up the timer spinner by populating it and set default to previous selection
     * Also adds the callback for spinner value changed
     *
     * @author Luca Crema
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
     * Searches for the current timeout value with {@link PreferencesManager}, checks every timer_values resources array and
     * finds the index of the current timeout time
     *
     * @return the found timer value
     * @author Luca Crema
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
     * Creates the dialog used to insert a non empty password or exit/abort
     *
     * @param command Specified type of dialog that should be opened, represented by an int value
     * @throws IllegalCommandException usually thrown when the dialog command passed is not valid
     * @author Alberto Ursino
     */
    void openDialog(int command) throws IllegalCommandException {
        switch (command) {
            case CHANGE_PASS_COMMAND:
                PasswordDialog passwordDialog = new PasswordDialog(CHANGE_PASS_COMMAND);
                passwordDialog.show(getSupportFragmentManager(), "Device Password");
                break;
            default:
                throw new IllegalCommandException();
        }
    }

}
