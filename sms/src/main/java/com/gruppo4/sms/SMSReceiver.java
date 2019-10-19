package com.gruppo4.sms;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReciever";
	private static final String PDU_TYPE = "pdus";

	/**
	 * This method is subscribed to the intent of a message received, and will be called whenever a new message is received.
	 * @param context
	 * @param intent
	 */
	@TargetApi(Build.VERSION_CODES.M)
	@Override
	public void onReceive(Context context, Intent intent) {
		// Get the SMS message.
		Bundle bundle = intent.getExtras();
		android.telephony.SmsMessage[] messages;
		String format = bundle.getString("format");
		// Retrieve the SMS message received.
		Object[] pdus = (Object[]) bundle.get(PDU_TYPE);
		if (pdus != null) {
			// Check the Android version.
			boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
			// Fill the messages array.
			messages = new android.telephony.SmsMessage[pdus.length];
			for (int i = 0; i < pdus.length; i++) {
				// Check Android version and use appropriate createFromPdu.
				if (isVersionM) {
					// If Android version M or newer:
					messages[i] = android.telephony.SmsMessage.createFromPdu((byte[]) pdus[i], format);
				} else {
					// If Android version L or older:
					messages[i] = android.telephony.SmsMessage.createFromPdu((byte[]) pdus[i]);
				}

				//Create a library packet class instance.
				SMSPacket packet = SMSPacket.parseSMSPacket(messages[i].getMessageBody());
				if (packet != null){
					//A packet will be null only if the message is not formatted correctly
					SMSController.onReceive(packet, messages[i].getOriginatingAddress());
				}
			}
		}
	}

}
