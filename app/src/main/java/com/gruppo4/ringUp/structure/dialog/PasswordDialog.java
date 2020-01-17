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
 * Class used to manage the password entry dialog
 *
 * @author Alberto Ursino with useful help: https://www.youtube.com/watch?v=ARezg1D9Zd0
 */
public class PasswordDialog extends AppCompatDialogFragment {

    private PasswordDialogListener passwordDialogListener;
    private static final int CHANGE_PASS_COMMAND = 0;
    private static final int SET_PASS_COMMAND = 1;
    private static final String SET_PASS_DELIVERY = "Set your device password";
    private static final String CHANGE_PASS_DELIVERY = "Change your device password";
    private Context context;
    private int command;

    /**
     * @param command to manage
     * @param context of the application
     */
    public PasswordDialog(int command, Context context) {
        this.command = command;
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.password_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        final TextView dialogTitle = view.findViewById(R.id.password_dialog_title);
        final EditText fieldToFill = view.findViewById(R.id.devicePassword);
        final Button positiveButton = view.findViewById(R.id.set);
        final Button negativeButton = view.findViewById(R.id.exit);

        //Fix the dialog title
        if (command == SET_PASS_COMMAND) {
            dialogTitle.setText(SET_PASS_DELIVERY);
        } else if (command == CHANGE_PASS_COMMAND) {
            dialogTitle.setText(CHANGE_PASS_DELIVERY);
        }

        positiveButton.setOnClickListener(v -> {
            String password = fieldToFill.getText().toString();
            if (password.isEmpty()) {
                Toast.makeText(context, getString(R.string.toast_password_absent), Toast.LENGTH_SHORT).show();
            } else if (command == SET_PASS_COMMAND) {
                passwordDialogListener.onPasswordSet(password, context);
                dismiss();
            } else if (command == CHANGE_PASS_COMMAND) {
                passwordDialogListener.onPasswordChanged(password, context);
                dismiss();
            }
        });

        negativeButton.setOnClickListener(v -> {
            if (command == SET_PASS_COMMAND) {
                passwordDialogListener.onPasswordNotSet(context); // <-- listener calling
                dismiss();
            } else if (command == CHANGE_PASS_COMMAND) {
                passwordDialogListener.onPasswordNotChanged(context); // <-- listener calling
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
