package com.gruppo4.sms.dataLink;

import android.content.Context;

import com.gruppo4.communication.CommunicationHandler;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

/**
 * Facade for SMSHandler
 */
public class SMSManager extends CommunicationHandler<SMSMessage> {

    /**
     * Singleton variable
     */
    private static SMSManager instance;
    private Context ctx;

    public static SMSManager getInstance(Context ctx) {
        if (instance == null)
            instance = new SMSManager();

        instance.ctx = ctx;
        return instance;
    }

    /**
     * @param appCode a integer code.
     * @return true if the parameter application code is valid.
     */
    public static boolean checkApplicationCodeIsValid(int appCode) {
        return SMSHandler.checkApplicationCodeIsValid(appCode);
    }

    /**
     * Checks if SEND_SMS permission is granted
     *
     * @param ctx a valid context
     * @return true if permission is granted
     */
    public static boolean checkSendPermission(Context ctx) {
        return SMSHandler.checkSendPermission(ctx);
    }

    /**
     * Checks if RECEIVE_SMS permission is granted
     *
     * @param ctx a valid context
     * @return true if permission is granted
     */
    public static boolean checkReceivePermission(Context ctx) {
        return SMSHandler.checkReceivePermission(ctx);
    }

    /**
     * Checks if both SEND_SMS & RECEIVE_SMS permissions are granted
     *
     * @param ctx a valid context
     * @return true if both permissions are granted
     */
    public static boolean checkPermissions(Context ctx) {
        return SMSHandler.checkPermissions(ctx);
    }

    /**
     * Sets up the library, should be called only when you first install the app
     *
     * @param applicationCode
     */
    public void setup(int applicationCode) {
        SMSHandler.setup(ctx, applicationCode);
    }

    /**
     * Sends a message via SMS
     *
     * @param message
     */
    @Override
    public void sendMessage(SMSMessage message) {
        this.sendMessage(message, null);
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

    @Override
    protected void callReceivedMessageListener(SMSMessage message) {
        if (this.receivedMessageListener != null)
            this.receivedMessageListener.onMessageReceived(message);
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

    /**
     * @return identifier in the sms communication channel for the current app
     */
    public int getApplicationCode() {
        return SMSHandler.getApplicationCode(ctx);
    }

}
