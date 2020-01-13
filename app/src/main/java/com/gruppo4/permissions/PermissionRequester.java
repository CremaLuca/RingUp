package com.gruppo4.permissions;

import android.content.pm.PackageManager;

/**
 * Interface that defines a class that takes a description (as String or as a layout) and an array of permission and shows a dialog or an activity with the description
 * and the button to continue that is used to be granted those permissions as parameter.
 *
 * @author Luca Crema
 * @since 13/01/2019
 */
public interface PermissionRequester {

    /**
     * Shows to the user a view with a description text to let him know why such permissions are used by the app, then shows the default Android permission request dialog.
     *
     * @param description Text that should contain the reasons why the app is using those permissions.
     * @param permissions Array of strings taken from {@link android.Manifest.permission}, the permissions to ask when the user read the description.
     */
    void requestPermission(String description, String[] permissions);

    /**
     * Shows to the user a view with the layout passed as parameter to let him know why such permissions are used by the app, then shows the default Android permission request dialog.
     *
     * @param layoutResourceId The resource id of the layout to show.
     * @param permissions      Array of strings taken from {@link android.Manifest.permission}, the permissions to ask when the user read the description.
     */
    void requestPermission(int layoutResourceId, String[] permissions);


    interface PermissionRequestListener {

        /**
         * Callback for permission request.
         *
         * @param permissions  The requested permissions.
         * @param grantResults Whether the permissions have been granted or not. Could be {@link PackageManager#PERMISSION_GRANTED} or {@link PackageManager#PERMISSION_DENIED}.
         */
        void onPermissionsRequested(String[] permissions, int[] grantResults);
    }
}
