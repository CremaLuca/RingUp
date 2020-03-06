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
 * @author Alberto Ursino feat. Francesco Bau'
 */
public class PermissionsHandler {

    public static final int REQUEST_CODE = 0;

    /**
     * Checks if all permissions are granted.
     *
     * @param context     The target Context. It can't be null.
     * @param permissions The permission(s) to check.
     * @return false if the app has at least 1 non-granted permission, true otherwise.
     * @author Francesco Bau', helped by Alberto Ursino.
     */
    public static boolean checkPermissions(@NonNull Context context, @Nullable String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (isNotGranted(context, permission))
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
            for (String permission : permissions) {
                if (isNotGranted(context, permission)) {
                    deniedPermissions.add(permission);
                    arrayLength++;
                }
            }
        }
        return deniedPermissions.toArray(new String[arrayLength]);
    }

    /**
     * Checks if a single permission is NOT granted.
     *
     * @param context    The target Context. It can't be null.
     * @param permission The permission to check.
     * @return true if a permission is NOT granted, false otherwise.
     * @author Francesco Bau'
     */
    private static boolean isNotGranted(@NonNull Context context, @Nullable String permission) {
        if(permission==null)
            return false;
        return context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED;
    }

}
