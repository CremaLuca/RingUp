package com.gruppo4.sms.dataLink.listeners;

import android.content.Context;

import com.gruppo4.communication.dataLink.listeners.ReceivedMessageListener;
import com.gruppo4.sms.dataLink.SMSMessage;

public interface SMSReceivedListener extends ReceivedMessageListener<SMSMessage> {

    void onMessageReceived(SMSMessage message, Context ctx);
}
