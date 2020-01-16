package com.gruppo4.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

/**
 * Class used to manage permissions.
 * Through this class you can request permissions, check them and other useful things such as get all denied permissions.
 *
 * @author Alberto Ursino
 */
public class PermissionsHandler {

    private static final int REQUEST_CODE = 0;

    /**
     * Checks if permissions are granted, if not then requests not granted ones to the user.
     *
     * @param permissions The permission to check.
     * @author Francesco Bau', helped by Alberto Ursino.
     */
    public void requestPermissions(Activity activity, Context context, String[] permissions) {
        if (!checkAllPermissions(context, permissions))
            activity.requestPermissions(getDeniedPermissions(context, permissions), REQUEST_CODE);
    }

    /**
     * Checks if all permissions are granted.
     *
     * @param permissions The permission(s) to check. It can't be null.
     * @return true if the app has all permissions granted, false otherwise.
     * @author Francesco Bau', helped by Alberto Ursino.
     */
    public boolean checkAllPermissions(Context context, @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (!isGranted(context, permission))
                return false;
        }
        return true;
    }

    /**
     * Checks which permissions are NOT granted.
     *
     * @param permissions The permission(s) to check.
     * @return The NOT granted permission(s).
     * @author Francesco Bau'
     * @author Alberto Ursino
     */
    public String[] getDeniedPermissions(Context context, String[] permissions) {
        String[] deniedPermissions = new String[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            if (!isGranted(context, permissions[i]))
                deniedPermissions[i] = permissions[i];
        }
        return deniedPermissions;
    }

    /**
     * Checks if a single permission is granted.
     *
     * @param permission The permission(s) to check.
     * @return true if the app has all permissions granted, false otherwise.
     * @author Francesco Bau'
     */
    private boolean isGranted(Context context, String permission) {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

}
