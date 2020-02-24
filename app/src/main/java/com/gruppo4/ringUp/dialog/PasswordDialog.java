package com.gruppo4.ringUp.dialog;

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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.gruppo4.ringUp.R;

/**
 * This class allows you to create and manage a personalized alert dialog.
 * The class asks, through the constructor, for a command and a context, respectively they will be used to define the type of dialog to be obtained and to manage the calls to the listeners.
 * <p>
 * {@link #command}(s) available:
 * - {@link #SET_PASS_COMMAND}: two buttons dialog with its correspondent title and listeners;
 * - {@link #CHANGE_PASS_COMMAND}: two buttons dialog with its correspondent title and listeners;
 * <p>
 * Each button will trigger a listener, e.g for the dialog created by the {@link #SET_PASS_COMMAND} we have:
 * - POSITIVE BUTTON -> {@link SetPasswordListener#onPasswordSet(String, Context)};
 * - NEGATIVE BUTTON -> {@link SetPasswordListener#onPasswordNotSet(Context)}.
 *
 * @author Alberto Ursino with useful help: https://www.youtube.com/watch?v=ARezg1D9Zd0
 */
public class PasswordDialog extends AppCompatDialogFragment {

    //Listeners
    private ChangePasswordListener changePasswordListener;
    private SetPasswordListener passwordSetListener;

    //Commands
    public static final int CHANGE_PASS_COMMAND = 0;
    public static final int SET_PASS_COMMAND = 1;

    //Dialog titles
    private TextView dialogTitle;
    private static final int SET_PASS_TITLE = R.string.set_device_password;
    private static final int CHANGE_PASS_TITLE = R.string.change_device_password;

    private EditText passwordField;

    private Context context;
    int command;

    /**
     * Constructor used to capture a {@link #command} and a {@link #context}.
     *
     * @param command passed by the calling method
     * @param context of the application
     * @author Albeto Ursino
     */
    public PasswordDialog(int command, Context context) {
        this.command = command;
        this.context = context;
    }

    /**
     * Android method used to create a personalized alert dialog.
     *
     * @author Implemented by Alberto Ursino
     * @see DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.password_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        dialogTitle = view.findViewById(R.id.password_dialog_title);
        passwordField = view.findViewById(R.id.devicePassword);
        Button positiveButton = view.findViewById(R.id.set);
        Button negativeButton = view.findViewById(R.id.exit);

        fixTitle(command);

        //----------POSITIVE BUTTON----------
        positiveButton.setOnClickListener(v -> {
            String password = passwordField.getText().toString();
            if (password.isEmpty()) {
                Toast.makeText(context, getString(R.string.toast_password_absent), Toast.LENGTH_SHORT).show();
            } else if (command == SET_PASS_COMMAND) {
                passwordSetListener.onPasswordSet(password, context); // <-- password-set-listener calling
                dismiss();
            } else if (command == CHANGE_PASS_COMMAND) {
                changePasswordListener.onPasswordChanged(password, context); // <-- password-changed listener calling
                dismiss();
            }
        });

        //----------NEGATIVE BUTTON----------
        negativeButton.setOnClickListener(v -> {
            if (command == SET_PASS_COMMAND) {
                passwordSetListener.onPasswordNotSet(context); // <-- password-not-set listener calling
                dismiss();
            } else if (command == CHANGE_PASS_COMMAND) {
                changePasswordListener.onPasswordNotChanged(context); // <-- password-not-changed listener calling
                dismiss();
            }
        });

        //The dialog does not close if the user clicks outside of it
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    /**
     * @param command The title is fixed based on this command
     * @author Alberto Ursino
     */
    private void fixTitle(int command) {
        switch (command) {
            case (CHANGE_PASS_COMMAND):
                dialogTitle.setText(getString(CHANGE_PASS_TITLE));
                break;
            case (SET_PASS_COMMAND):
                dialogTitle.setText(getString(SET_PASS_TITLE));
                break;
            default:
        }
    }

    /**
     * @author Implemented by Alberto Ursino
     * @see Fragment#onAttach(android.content.Context)
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //Setting listeners
        if (command == SET_PASS_COMMAND)
            passwordSetListener = (SetPasswordListener) context;
        else
            changePasswordListener = (ChangePasswordListener) context;
    }
}
