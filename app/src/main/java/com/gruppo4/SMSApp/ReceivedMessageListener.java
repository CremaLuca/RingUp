package com.gruppo4.SMSApp;

import android.content.Context;

import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;

/**
 *
 */
public class ReceivedMessageListener implements SMSReceivedListener {

    Context ctx;

    public ReceivedMessageListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onMessageReceived(SMSMessage message) {
        //RingCommand command = RingHandler.parseString(message.getData(), message.getPeer())
        //if(command != null){ AppManager.onRingCommandReceived(ctx,command);}
    }
}
