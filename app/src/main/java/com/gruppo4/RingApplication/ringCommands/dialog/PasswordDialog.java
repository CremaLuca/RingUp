package com.gruppo4.RingApplication.ringCommands.dialog;

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

import com.gruppo4.RingApplication.MainActivity;
import com.gruppo4.RingApplication.R;

import org.w3c.dom.Text;

/**
 * @author Alberto Ursino
 * Useful help: https://www.youtube.com/watch?v=ARezg1D9Zd0
 */
public class PasswordDialog extends AppCompatDialogFragment {

    private PasswordDialogListener passwordDialogListener;
    private static int command;
    private static final int CHANGE_PASS_COMMAND = 0;
    private static final int SET_PASS_COMMAND = 1;

    public PasswordDialog(int command) {
        this.command = command;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.password_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        final Context context = getContext();
        final TextView DELIVERY_MESSAGE = view.findViewById(R.id.delivery);
        final EditText DEVICE_PASSWORD = view.findViewById(R.id.devicePassword);
        final Button SET_BUTTON = view.findViewById(R.id.setPass);
        final Button EXIT_BUTTON = view.findViewById(R.id.exit);

        //Fix the dialog relying to the command received in the constructor
        if (isCommandSetPass(command)) {
            EXIT_BUTTON.setText("Exit");
            DELIVERY_MESSAGE.setText("Set your device password");
        } else if (isCommandChangePass(command)) {
            EXIT_BUTTON.setText("Abort");
            DELIVERY_MESSAGE.setText("Change your password device");
        }

        SET_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = DEVICE_PASSWORD.getText().toString();
                if (passwordIsEmpty(password)) {
                    Toast.makeText(context, "Password is empty", Toast.LENGTH_SHORT).show();
                } else {
                    passwordDialogListener.applyText(password, context);
                    //Close the dialog
                    dismiss();
                }
            }
        });

        EXIT_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCommandSetPass(command))
                    System.exit(0);
                else if (isCommandChangePass(command)) {
                    //Do nothing
                    dismiss();
                }
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

    /**
     * Check if the command is CHANGE_PASSWORD
     */
    public boolean isCommandChangePass(int command) {
        return command == CHANGE_PASS_COMMAND;
    }

    /**
     * Check if the command is SET_PASSWORD
     */
    public boolean isCommandSetPass(int command) {
        return command == SET_PASS_COMMAND;
    }

}
