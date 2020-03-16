package com.gruppo4.ringUp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.eis.smslibrary.listeners.SMSSentListener;
import com.google.android.material.snackbar.Snackbar;
import com.gruppo4.ringUp.R;
import com.gruppo4.ringUp.dialog.ChangePasswordListener;
import com.gruppo4.ringUp.dialog.PasswordDialog;
import com.gruppo4.ringUp.structure.AppManager;
import com.gruppo4.ringUp.structure.NotificationHandler;
import com.gruppo4.ringUp.structure.PasswordManager;
import com.gruppo4.ringUp.structure.PermissionsHandler;
import com.gruppo4.ringUp.structure.ReceivedMessageListener;
import com.gruppo4.ringUp.structure.RingCommand;

/**
 * @author Gruppo4
 */
public class MainActivity extends AppCompatActivity implements ChangePasswordListener {

    private Button ringButton;
    private TextView adviceTextView;
    private EditText phoneNumberField, passwordField;
    private static final int WAIT_TIME_RING_BTN_ENABLED = 10 * 1000;
    private static int timerValue = WAIT_TIME_RING_BTN_ENABLED;
    private static String adviceText = "Wait " + timerValue + " seconds for a new ring";
    static final String DIALOG_TAG = "Device Password";
    private static final int PICK_CONTACT = 1;
    private static final int COUNTDOWN_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();

        AppManager.getInstance().setup(context);

        Intent preActIntent;
        //If the permissions are not given, the permissionsActivity is opened
        if (!PermissionsHandler.checkPermissions(context, PermissionsActivity.permissions)) {
            preActIntent = new Intent(context, PermissionsActivity.class);
            startActivity(preActIntent);
            this.finish();
            //If the permissions are all granted then checks if a password is stored in memory: if NOT then open the instructionsActivity
        } else if (!PasswordManager.isPassSaved(context)) {
            preActIntent = new Intent(context, InstructionsActivity.class);
            startActivity(preActIntent);
            this.finish();
        }

        //Only if the activity is started by a service
        startFromService();

        //Setting up the custom listener in order to receive messages
        SMSManager.getInstance().setReceivedListener(ReceivedMessageListener.class, context);
        phoneNumberField = findViewById(R.id.phone_number_field);
        passwordField = findViewById(R.id.password_field);
        adviceTextView = findViewById(R.id.timer_text_view);
        ringButton = findViewById(R.id.ring_button);
        ringButton.setOnClickListener(v -> sendRingCommand());
    }

    //**************************************SEND_COMMAND**************************************

    /**
     * Method used to send the ring command through the {@link AppManager#sendRingCommand(Context, RingCommand, SMSSentListener)} method
     *
     * @author Alberto Ursino
     * @author Luca Crema
     */
    public void sendRingCommand() {
        String phoneNumber = phoneNumberField.getText().toString();
        String password = passwordField.getText().toString();

        if (password.isEmpty() && phoneNumber.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_pass_phone_number_absent), Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_phone_number_absent), Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_password_absent), Toast.LENGTH_SHORT).show();
        } else {
            try {
                //Creation of the ring command
                final RingCommand ringCommand = new RingCommand(new SMSPeer(phoneNumber), password);

                AppManager.getInstance().sendRingCommand(getApplicationContext(), ringCommand, (SMSMessage message, SMSMessage.SentState sentState) -> {
                    Snackbar.make(findViewById(R.id.main_activity_layout), getString(R.string.toast_message_sent_listener) + " " + phoneNumber, Snackbar.LENGTH_LONG).show();
                });
                ringButton.setEnabled(false);
                adviceTextView.setText(adviceText);

                //Button disabling
                new CountDownTimer(WAIT_TIME_RING_BTN_ENABLED, COUNTDOWN_INTERVAL) {

                    public void onTick(long millisUntilFinished) {
                        timerValue = (int) millisUntilFinished;
                        adviceTextView.setText(getString(R.string.ten_second_timer_timer) + " " + timerValue / COUNTDOWN_INTERVAL);
                    }

                    public void onFinish() {
                        if (!ringButton.isEnabled())
                            ringButton.setEnabled(true);
                        adviceTextView.setText("");
                        timerValue = WAIT_TIME_RING_BTN_ENABLED;
                    }
                }.start();

            } catch (InvalidTelephoneNumberException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_invalid_phone_number), Toast.LENGTH_SHORT).show();
            }


        }
    }

    //**************************************MENU**************************************

    /**
     * Method used to show up the {@link menu/app_menu.xml}
     *
     * @author Alberto Ursino
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Called when the user selects an item from the {@link menu/app_menu.xml}
     *
     * @author Alberto Ursino
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_password_menu_item:
                openDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method to open the system address book
     *
     * @param view The view calling the method
     * @author Alessandra Tonin
     */
    public void openAddressBook(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    /**
     * Method to handle the picked contact
     *
     * @param requestCode The code of the request
     * @param resultCode  The result of  the request
     * @param data        The data of the result
     * @author Alessandra Tonin
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                String number = "";
                Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();
                String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                if (hasPhone.equals("1")) {
                    Cursor phones = getContentResolver().query
                            (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        number = phones.getString(phones.getColumnIndex
                                (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
                    }
                    phones.close();
                    //Put the number in the phoneNumberField
                    phoneNumberField.setText(number);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_contact_has_no_phone_number), Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        }
    }

    //**************************************PASSWORD_DIALOG**************************************

    /**
     * Creates the dialog used to insert a non empty password or exit/abort.
     * Dialog is created through the {@link PasswordDialog} class
     *
     * @author Alberto Ursino
     */
    void openDialog() {
        PasswordDialog passwordDialog;
        passwordDialog = new PasswordDialog(PasswordDialog.CHANGE_PASS_COMMAND, getApplicationContext());
        passwordDialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }

    /**
     * For more information: {@link ChangePasswordListener#onPasswordChanged(String, Context)}
     *
     * @author Alberto Ursino
     */
    @Override
    public void onPasswordChanged(String password, Context context) {
        Toast.makeText(context, getString(R.string.toast_password_changed), Toast.LENGTH_SHORT).show();
        PasswordManager.setPassword(context, password);
    }

    /**
     * For more information: {@link ChangePasswordListener#onPasswordNotChanged(Context)}
     *
     * @author Alberto Ursino
     */
    @Override
    public void onPasswordNotChanged(Context context) {
        Toast.makeText(context, getString(R.string.toast_password_not_changed), Toast.LENGTH_LONG).show();
    }


    /**
     * Updates intent obtained from a service's call
     *
     * @param intent to handle
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        startFromService();
    }

    /**
     * Manages action from intent
     */
    private void startFromService() {
        Log.d("MainActivity", "startFromService called");
        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case NotificationHandler.PRESSED_NOTIFICATION_ACTION: {
                    createStopRingDialog();
                    Log.d("MainActivity", "Creating StopRingDialog...");
                    break;
                }
                default:
                    break;
            }
        } else {
            Log.d("MainActivity", "getIntent, in startFromService method, returns a null intent");
        }
    }

    /**
     * Creates and shows AlertDialog with one option:
     * [stop] --> stop the ringtone and cancel the notification
     */
    private void createStopRingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getString(R.string.text_stop_ring_dialog));
        builder.setCancelable(false);

        builder.setPositiveButton(
                getString(R.string.text_notification_button), (dialogInterface, i) -> {
                    AppManager.getInstance().stopRingtone(getApplicationContext());
                    dialogInterface.dismiss();
                }
        );

        builder.create().show();
    }

}


