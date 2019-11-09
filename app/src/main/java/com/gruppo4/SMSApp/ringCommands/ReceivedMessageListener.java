package com.gruppo4.SMSApp.ringCommands;

import android.content.Context;

import com.gruppo4.SMSApp.AppManager;
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
        RingCommand ringCommand = RingHandler.parseString(message.getPeer(), message.getData());
        if (ringCommand != null)
            AppManager.onRingCommandReceived(ctx, ringCommand);
    }
}
