package com.gruppo4.ringUp.structure.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.ringUp.R;
import com.gruppo4.ringUp.structure.AppManager;
import com.gruppo4.ringUp.structure.RingCommand;
import com.gruppo4.ringUp.structure.RingCommandHandler;

import androidx.appcompat.app.AppCompatDialogFragment;

import static android.app.Activity.RESULT_OK;

public class CommandDialog extends AppCompatDialogFragment {

    private static final int PICK_CONTACT_CODE = 420;
    private EditText phoneNumberTextField;
    private EditText passwordTextField;
    private Context ctx;
    private View activityView;

    /**
     * @param currentContext can be application context
     */
    public CommandDialog(Context currentContext, View activityMainView) {
        this.ctx = currentContext;
        this.activityView = activityMainView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.command_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.give_permissions_button_text, null)
                .setNegativeButton(R.string.exit_dialog_button, (dialog, which) -> dialog.cancel());

        phoneNumberTextField = view.findViewById(R.id.telephone_number_editText);
        passwordTextField = view.findViewById(R.id.password_editText);
        view.findViewById(R.id.select_contact_imageButton).setOnClickListener(v -> openAddressBook(v));

        //The alert dialog won't be closed if the user presses outside it
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> onPositiveDialogButton());
    }

    private void onPositiveDialogButton() {
        Log.v("CommandDialog", "Positive button clicked");
        String phoneNumber = phoneNumberTextField.getText().toString();
        String password = passwordTextField.getText().toString();
        if (password.isEmpty() && phoneNumber.isEmpty()) {
            Toast.makeText(ctx, getString(R.string.toast_pass_phone_number_absent), Toast.LENGTH_SHORT).show();
            return;
        } else if (phoneNumber.isEmpty()) {
            Toast.makeText(ctx, getString(R.string.toast_phone_number_absent), Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(ctx, getString(R.string.toast_password_absent), Toast.LENGTH_SHORT).show();
            return;
        }
        SMSPeer address;
        try {
            address = new SMSPeer(phoneNumber);
        } catch (InvalidTelephoneNumberException e) {
            Toast.makeText(ctx, "Invalid phone number", Toast.LENGTH_LONG).show();
            return;
        }

        final RingCommand ringCommand = new RingCommand(address, RingCommandHandler.SIGNATURE + password);

        AppManager.getInstance().sendCommand(ctx, ringCommand, null);
        //TODO: Have a callback for sent command, cannot be done here because dialog is killed
        dismiss();
    }

    /**
     * Method to open the system address book
     *
     * @param view The view calling the method
     * @author Alessandra Tonin
     */
    public void openAddressBook(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_CODE);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_CODE) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                String number = "";
                Cursor cursor = ctx.getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();
                String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                if (hasPhone.equals("1")) {
                    Cursor phones = ctx.getContentResolver().query
                            (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        number = phones.getString(phones.getColumnIndex
                                (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
                    }
                    phones.close();
                    //Put the number in the phoneNumberField
                    phoneNumberTextField.setText(number);
                } else {
                    Toast.makeText(ctx, getString(R.string.toast_contact_has_no_phone_number), Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        }
    }


}
