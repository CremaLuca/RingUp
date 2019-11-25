package com.gruppo4.sms.dataLink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gruppo4.sms.dataLink.background.SMSKeepAliveService;

/**
 * Starts service and keeps broadcastReceiver alive even if the phone is restarted
 *
 * @author Marco Tommasini
 */

public class SMSAutoStartReceiver extends BroadcastReceiver {

    //TODO: test and fix (not working)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("SMSAutoStartReceiver", "Called onReceive");
        Intent service = new Intent(context, SMSKeepAliveService.class);
        context.startService(service);
    }
}
