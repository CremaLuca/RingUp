package com.gruppo4.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SMSDelivered extends BroadcastReceiver {

    // Called when Delivered event occurs
    //TODO: update msg delivered status
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:

                break;
            case Activity.RESULT_CANCELED:

                break;

        }
    }
}
