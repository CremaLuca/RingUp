package com.gruppo4.ringUp.permissions;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Class used to manage permissions.
 * Through this class you can request permissions, check them and other useful things such as get all denied permissions.
 *
 * @author Alberto Ursino
 */
public class PermissionsHandler {

    public static final int REQUEST_CODE = 0;

    /**
     * Checks if all permissions are granted.
     *
     * @param context     The target Context. It can't be null.
     * @param permissions The permission(s) to check.
     * @return false if the app has at least 1 permission denied, true otherwise.
     * @author Francesco Bau', helped by Alberto Ursino.
     */
    public static boolean checkPermissions(@NonNull Context context, @Nullable String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (isGranted(context, permission))
                    return false;
            }
        }
        return true;
    }

    /**
     * Checks which permissions are NOT granted.
     *
     * @param context     The target Context. It can't be null.
     * @param permissions The permission(s) to check.
     * @return The NOT granted permission(s).
     * @author Francesco Bau'
     * @author Alberto Ursino
     */
    public static String[] getDeniedPermissions(@NonNull Context context, @Nullable String[] permissions) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        int arrayLength = 0;
        if (permissions != null) {
            for (int i = 0; i < permissions.length; i++) {
                if (isGranted(context, permissions[i])) {
                    deniedPermissions.add(permissions[i]);
                    arrayLength++;
                }
            }
        }
        return deniedPermissions.toArray(new String[arrayLength]);
    }

    /**
     * Checks if a single permission is granted.
     *
     * @param context    The target Context. It can't be null.
     * @param permission The permission to check. It can't be null.
     * @return true if the permission is granted, false otherwise.
     * @author Francesco Bau'
     */
    private static boolean isGranted(@NonNull Context context, @NonNull String permission) {
        return context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED;
    }

}
