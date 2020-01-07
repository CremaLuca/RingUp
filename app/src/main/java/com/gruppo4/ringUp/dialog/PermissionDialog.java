package com.eis.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * It triggers a Dialog of a certain permission.
 *
 * @author Francesco Bau'
 * @version 1.0
 * @since 23/12/2019
 */
public class PermissionDialog {

    private Context activityContext;

    /**
     *
     * @param activity The Activity's Context. It can't be null.
     * @param permission The permission requested. It can't be null.
     */
    public PermissionDialog(@NonNull Activity activity, @NonNull String permission) {
        this.activityContext = activity;

        // if(context instanceof Activity){this.context=context} else throw new IllegalArgumentException("Must be an Activity's Context.");
        // context.checkSelfPermission(permission); //Requires API 23
        if (ContextCompat.checkSelfPermission(this.activityContext, permission) != PackageManager.PERMISSION_GRANTED) {
            //permission is NOT granted
            ActivityCompat.requestPermissions((Activity) this.activityContext, new String[]{permission}, 1);
        }

        /**
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity, permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions((Activity) activity, new String[]{permission}, 1);

                // The callback method gets the result of the request.
            }
        } else {
            // Permission has already been granted
        }
        */

    }
}
