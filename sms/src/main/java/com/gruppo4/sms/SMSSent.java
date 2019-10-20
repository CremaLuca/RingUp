package com.gruppo4.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SMSSent extends BroadcastReceiver {

    /*
     Called when Sent event occurs
    TODO: update msg sent status
    */
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:

                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:

                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:

                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:

                break;

        }
    }
}
