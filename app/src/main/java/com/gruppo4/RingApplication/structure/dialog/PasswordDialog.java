package com.gruppo4.RingApplication.structure.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.gruppo4.RingApplication.R;

/**
 * @author Alberto Ursino
 * Useful help: https://www.youtube.com/watch?v=ARezg1D9Zd0
 */
public class PasswordDialog extends AppCompatDialogFragment {

    private PasswordDialogListener passwordDialogListener;
    private static int command;
    private static final int CHANGE_PASS_COMMAND = 0;
    private static final int SET_PASS_COMMAND = 1;
    private static final String SET_DELIVERY = "Set your device password";
    private static final String CHANGE_DELIVERY = "Change your password device";

    public PasswordDialog(int command) {
        PasswordDialog.command = command;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.password_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        final Context context = getContext();
        final TextView delivery_message = view.findViewById(R.id.delivery);
        final EditText device_password = view.findViewById(R.id.devicePassword);
        final Button set_button = view.findViewById(R.id.setPass);
        final Button close_button = view.findViewById(R.id.exit); //It can be the "EXIT" or the "ABORT" button

        /**
         * Fix the dialog interface depending on the command received in the constructor
         */
        if (isCommandSetPass(command)) {
            close_button.setText("Exit");
            delivery_message.setText(SET_DELIVERY);
        } else if (isCommandChangePass(command)) {
            close_button.setText("Abort");
            delivery_message.setText(CHANGE_DELIVERY);
        }

        set_button.setOnClickListener(v -> {
            String password = device_password.getText().toString();
            if (passwordIsEmpty(password)) {
                Toast.makeText(context, "Password is empty", Toast.LENGTH_SHORT).show();
            } else {
                passwordDialogListener.onPasswordSet(password, context);
                Toast.makeText(context, "Password saved", Toast.LENGTH_SHORT).show();
                //Close the dialog
                dismiss();
            }
        });

        close_button.setOnClickListener(v -> {
            if (isCommandSetPass(command))
                System.exit(0);
            else if (isCommandChangePass(command)) {
                //Do nothing
                Toast.makeText(context, "Nothing changed", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        //The alert dialog won't be closed if the user presses outside it
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        passwordDialogListener = (PasswordDialogListener) context;
    }

    /**
     * Check if the password is empty, true = yes, false = no
     */
    private static boolean passwordIsEmpty(String password) {
        return password.equals("");
    }

    /**
     * Check if the command is CHANGE_PASSWORD
     */
    public static boolean isCommandChangePass(int command) {
        return command == CHANGE_PASS_COMMAND;
    }

    /**
     * Check if the command is SET_PASSWORD
     */
    public static boolean isCommandSetPass(int command) {
        return command == SET_PASS_COMMAND;
    }

}
