package com.gruppo4.sms.dataLink.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Starts service and keeps broadcastReceiver alive even if the phone is restarted
 *
 * @author Marco Tommasini
 */

public class SMSAutoStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("SMSAutoStartReceiver", "Called onReceive");
        Intent service = new Intent(context, SMSKeepAliveService.class);
        context.startService(service);
    }
}
