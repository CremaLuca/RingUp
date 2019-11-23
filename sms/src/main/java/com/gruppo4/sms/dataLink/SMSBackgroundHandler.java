package com.gruppo4.sms.dataLink;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Keeps the broadcast receiver alive while the app is closed
 *
 * @author Luca Crema
 */
public class SMSBackgroundHandler {

    public static void onAppDestroy(Context context) {
        Log.v("SMSBackHandler", "Called onAppDestroy");
        Intent service = new Intent(context, SMSKillerService.class);
        context.startService(service);
    }

    public static void onAppCreate(Context context) {
        Log.v("SMSBackHandler", "Called onAppCreate");
        Intent service = new Intent(context, SMSKillerService.class);
        context.stopService(service);
    }


}
