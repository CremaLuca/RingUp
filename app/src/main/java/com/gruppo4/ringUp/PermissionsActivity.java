package com.gruppo4.ringUp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * [Insert class description here]
 *
 * @author Francesco Bau'
 * @author [Insert new author's full name]
 * @since 07/01/2020
 */
public class PermissionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: layout. setContentView è da modificare.
        setContentView(R.layout.activity_main);

        //TODO: Tutto. Non ci sto capendo.
        // Bisogna forse creare un oggetto di tipo PasswordManager come in MainActivity???
        if(!(checkSelfPermissions(TEXT_SERVICES_MANAGER_SERVICE) && isPassSaved()))
        openPreActivities();
    } else if(!(checkPermissions()))
    openPermissionsActivity();
else if(!(isPassSaved()))
    openInstructionActivity();

    }
}
