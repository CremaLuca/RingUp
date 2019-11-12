package com.gruppo4.sms.dataLink;

import android.content.Context;

import com.gruppo4.communication.CommunicationHandler;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

public class SMSManager extends CommunicationHandler<SMSMessage> {

    private static SMSManager instance;
    private Context ctx;

    public static SMSManager getInstance(Context ctx) {
        if (instance == null)
            instance = new SMSManager();

        instance.ctx = ctx;
        return instance;
    }

    @Override
    public void sendMessage(SMSMessage message) {
        this.sendMessage(message, null);
    }

    @Override
    protected void callReceivedMessageListener(SMSMessage message) {
        if (this.receivedMessageListener != null)
            this.receivedMessageListener.onMessageReceived(message);
    }

    /**
     * Overload for method sendMessage(SMSMessage message)
     *
     * @param message  message to be sent via SMS
     * @param listener called when a message is sent
     */
    public void sendMessage(SMSMessage message, SMSSentListener listener) {
        SMSHandler.sendMessage(ctx, message, listener);
    }

    /**
     * Sets up the librari, should be called only when you first install the app
     *
     * @param applicationCode
     */
    public void setup(int applicationCode) {
        SMSHandler.setup(ctx, applicationCode);
    }

    /**
     * @return true if setup(int applicationCode) has been called at least once from the app installation
     */
    public boolean isSetup() {
        try {
            SMSHandler.getApplicationCode(ctx);
        } catch (IllegalStateException e) {
            return false;
        }
        return true;
    }

    public int getApplicationCode() {
        return SMSHandler.getApplicationCode(ctx);
    }

}
