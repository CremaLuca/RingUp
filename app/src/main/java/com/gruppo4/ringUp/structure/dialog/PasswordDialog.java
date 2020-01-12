package com.gruppo4.ringUp.structure.dialog;

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

import com.gruppo4.ringUp.R;

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
    private Context context;

    /**
     * @param command to manage
     * @param context of the application
     */
    public PasswordDialog(int command, Context context) {
        PasswordDialog.command = command;
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.password_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        final TextView delivery_message = view.findViewById(R.id.delivery);
        final EditText field_to_fill = view.findViewById(R.id.devicePassword);
        final Button positive_button = view.findViewById(R.id.setPass);
        final Button negative_button = view.findViewById(R.id.exit);

        //Fix the dialog interface "negative" button depending on the command received in the constructor
        if (command == SET_PASS_COMMAND) {
            negative_button.setText("Exit");
            delivery_message.setText(SET_DELIVERY);
        } else if (command == CHANGE_PASS_COMMAND) {
            negative_button.setText("Abort");
            delivery_message.setText(CHANGE_DELIVERY);
        }

        positive_button.setOnClickListener(v -> {
            String password = field_to_fill.getText().toString();
            if (password.isEmpty()) {
                Toast.makeText(context, "Password is empty", Toast.LENGTH_SHORT).show();
            } else {
                passwordDialogListener.onPasswordSet(password, context);
                Toast.makeText(context, "Password saved", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        negative_button.setOnClickListener(v -> {
            if (command == SET_PASS_COMMAND) {
                //Instead of exiting the application a listener has been called
                passwordDialogListener.onPasswordNotSet();
                dismiss();
            } else if (command == CHANGE_PASS_COMMAND) {
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

}
