package com.gruppo4.ringUp.structure;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Class used to manage permissions.
 * Through this class you can request permissions, check them and other useful things such as
 * get all granted or denied permissions from a list.
 *
 * @author Alberto Ursino
 * @author Luca Crema
 */
public class PermissionsHandler {

    /**
     * Checks if all requested permissions are granted.
     *
     * @param permissionList The permission(s) to check. It can't be null.
     * @return true if the app has all permissions granted, false otherwise.
     * @author Francesco Bau', helped by Alberto Ursino.
     */
    public static boolean checkPermissions(@NonNull final Context context, @NonNull final String[] permissionList) {
        for (String permission : permissionList) {
            if (!isPermissionGranted(context, permission))
                return false;
        }
        return true;
    }

    /**
     * Checks which permissions are NOT granted.
     *
     * @param permissionList The permissions to check.
     * @return An array containing denied permissions (not granted) from the parameter's. Empty if they're all granted.
     * @author Francesco Bau'
     * @author Alberto Ursino
     */
    public static String[] getDeniedPermissions(@NonNull final Context context, @NonNull final String[] permissionList) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissionList) {
            if (!isPermissionGranted(context, permission))
                deniedPermissions.add(permission);
        }
        return deniedPermissions.toArray(new String[0]);
    }

    /**
     * Checks which permissions are granted.
     *
     * @param permissionList The permissions to check.
     * @return An array containing only the granted permissions. Empty if none is granted.
     * @author Luca Crema
     */
    public static String[] getGrantedPermissions(@NonNull final Context context, @NonNull final String[] permissionList) {
        ArrayList<String> grantedPermissions = new ArrayList<>();
        for (String permission : permissionList) {
            if (isPermissionGranted(context, permission))
                grantedPermissions.add(permission);

        }
        return grantedPermissions.toArray(new String[0]);
    }

    /**
     * Checks if a single permission is granted.
     *
     * @param permissionName The permission to check.
     * @return true if the permission is granted, false otherwise.
     * @author Francesco Bau'
     */
    private static boolean isPermissionGranted(@NonNull final Context context, @NonNull final String permissionName) {
        return context.checkSelfPermission(permissionName) == PackageManager.PERMISSION_GRANTED;
    }

}
