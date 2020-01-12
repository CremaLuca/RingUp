package com.gruppo4.ringUp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Class used to inform the user of which permissions the app needed and requires them
 *
 * @author Francesco Bau'
 * @author Alberto Ursino
 * @since 07/01/2020
 */
public class PermissionsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;

    //TODO: Cambiare bottone, prendendolo dal layout.
    Button dummyButton = new Button(getApplicationContext());

    static final String[] permissions = new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_CONTACTS}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);


        dummyButton.setOnClickListener(v -> requestPermissions(permissions));
    }

    /**
     * Checks if permissions are granted, if not then requests them to the user.
     *
     * @param permissions The permission to check.
     * @author Francesco Bau' , helped by Alberto Ursino.
     */
    public void requestPermissions(String[] permissions) {
        if (!checkAllPermissions(permissions))
            requestPermissions(permissions, REQUEST_CODE);
    }

    /**
     * Checks if all permissions are granted.
     *
     * @param permissions The permission(s) to check. It can't be null.
     * @return true if the app has all permissions granted, false otherwise.
     * @author Francesco Bau' , helped by Alberto Ursino.
     */
    public boolean checkAllPermissions(@NonNull String[] permissions) {
        for (String permission : permissions) {
            if (!isGranted(permission))
                return false;
        }
        return true;
    }

    /**
     * Checks if a single permission is granted.
     *
     * @param permission The permission(s) to check.
     * @return true if the app has all permissions granted, false otherwise.
     * @author Francesco Bau'
     */
    private boolean isGranted(String permission) {
        return getApplicationContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Checks which permissions are NOT granted.
     *
     * @param permissions The permission(s) to check.
     * @return The NOT granted permission(s).
     * @author Francesco Bau'
     */
    public ArrayList<String> getDeniedPermissions(String[] permissions) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (!isGranted(permission))
                deniedPermissions.add(permission);
        }
        return deniedPermissions;
    }

}
