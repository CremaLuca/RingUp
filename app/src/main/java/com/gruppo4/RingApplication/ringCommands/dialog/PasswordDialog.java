package com.gruppo4.RingApplication.ringCommands.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.password_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        final EditText DEVICE_PASSWORD = view.findViewById(R.id.devicePassword);
        final Button SET_BUTTON = view.findViewById(R.id.setPass);
        final Button EXIT_BUTTON = view.findViewById(R.id.exit);

        SET_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = DEVICE_PASSWORD.getText().toString();
                if (passwordIsEmpty(password)) {
                    Toast.makeText(getContext(), "Password is empty", Toast.LENGTH_SHORT).show();
                } else {
                    passwordDialogListener.applyText(password);
                    //Close the dialog
                    dismiss();
                }
            }
        });

        EXIT_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        passwordDialogListener = (PasswordDialogListener) context;
    }

    /**
     * Check if the password is empty, true = yes, false = no
     */
    private boolean passwordIsEmpty(String password) {
        return password.equals("");
    }
}
