package com.gruppo4.sms.dataLink;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;

import com.gruppo4.communication.dataLink.CommunicationHandler;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;
import com.gruppo_4.preferences.PreferencesManager;

import java.util.ArrayList;

/**
 * Handles communications between peers of a sms communication channel
 * Every message has the applicationID to prevent interfering with other apps using the same library
 *
 * @author Gruppo4
 */
public class SMSHandler extends CommunicationHandler<SMSMessage> {

    public static final String SENT_MESSAGE_INTENT_ACTION = "SMS_SENT";
    private static final String APPLICATION_CODE_PREFERENCES_KEY = "ApplicationCode";

    private static SMSHandler instance;
    private Context ctx;

    public static SMSHandler getInstance(Context context) {
        if (instance == null)
            instance = new SMSHandler();
        instance.ctx = context;
        return instance;
    }

    /**
     * @param applicationCode a integer code.
     * @return true if the parameter application code is valid.
     */
    public static boolean checkApplicationCodeIsValid(int applicationCode) {
        return applicationCode > 0 && applicationCode < 1000;
    }

    /**
     * Checks if SEND_SMS permission is granted
     *
     * @param ctx a valid context
     * @return true if permission is granted
     */
    public static boolean checkSendPermission(Context ctx) {
        boolean result = ctx.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        Log.d("1", "return del metodo checkSendPermission: " + result);
        return result;
    }

    /**
     * Checks if RECEIVE_SMS permission is granted
     *
     * @param ctx a valid context
     * @return true if permission is granted
     */
    public static boolean checkReceivePermission(Context ctx) {
        boolean result = ctx.checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        Log.d("1", "return del metodo checkSendPermission: " + result);
        return result;
    }

    /**
     * Checks if both SEND_SMS & RECEIVE_SMS permissions are granted
     *
     * @param ctx a valid context
     * @return true if both permissions are granted
     */
    public static boolean checkPermissions(Context ctx) {
        return checkSendPermission(ctx) && checkReceivePermission(ctx);
    }

    /**
     * Writes in the memory the application code.
     *
     * @param ctx             current app or service context.
     * @param applicationCode a valid application code, checked with checkApplicationCodeIsValid.
     */
    private static void setApplicationCode(Context ctx, int applicationCode) {
        PreferencesManager.setInt(ctx, APPLICATION_CODE_PREFERENCES_KEY, applicationCode);
    }

    /**
     * Setup the handler, checks permissions and sets the application code
     *
     * @param applicationCode an identifier for the current application
     */
    public void setup(int applicationCode) {
        if (!checkApplicationCodeIsValid(applicationCode))
            throw new IllegalStateException("Application code not valid, check it with checkApplicationCodeIsValid() first");
        setApplicationCode(ctx, applicationCode);
    }

    /**
     * Send a SMSMessage, multiple packets could be sent
     * Requires Manifest.permission.SEND_SMS permission
     *
     * @param message  the message to be sent via SMS
     * @param listener called when the message is completely sent to the provider
     */
    public void sendMessage(final SMSMessage message, SMSSentListener listener) {
        ArrayList<String> dividedBySystem = SmsManager.getDefault().divideMessage(message.getData());
        if (dividedBySystem.size() > 1)
            throw new IllegalStateException("The message is too long (???) how can it be? Are we dividing it in a wrong way?");
        String smsContent = SMSMessageHandler.getInstance().getOutput(message);

        SMSSentBroadcastReceiver onSentReceiver = new SMSSentBroadcastReceiver(message, listener);
        ctx.registerReceiver(onSentReceiver, new IntentFilter(SENT_MESSAGE_INTENT_ACTION));
        PendingIntent sentPI = PendingIntent.getBroadcast(ctx, 0, new Intent(SENT_MESSAGE_INTENT_ACTION), 0);

        SMSCore.sendMessage(smsContent, message.getPeer().getAddress(), sentPI);
    }

    /**
     * Gets the identifier application code from the memory.
     *
     * @return the current application code
     */
    public int getApplicationCode() {
        int appCode = PreferencesManager.getInt(ctx, APPLICATION_CODE_PREFERENCES_KEY);
        if (appCode < 0)
            throw new IllegalStateException("Unable to perform the request, the SMS library has never been setup, call the setup() method at least once");
        return appCode;
    }

    /**
     * Overload for checkPermissions(Context)
     *
     * @return true if both SEND_SMS and RECEIVE_SMS permissions are granted
     */
    public boolean checkPermissions() {
        return checkPermissions(ctx);
    }

    /**
     * Send a SMSMessage, multiple packets could be sent
     * Requires Manifest.permission.SEND_SMS permission
     *
     * @param message the message to be sent via SMS
     */
    @Override
    public void sendMessage(SMSMessage message) {
        this.sendMessage(message, null);
    }

    /**
     * Sets a listener service class to be instantiated and started on a message arrival
     *
     * @param service A class reference to a service that extends {@link SMSReceivedListener}
     * @param <T>     The class type to be instantiated and started
     */
    public <T extends SMSReceivedListener> void setReceivedMessageListener(Class<T> service) {
        Log.v("SMSHandler", "Setting the received message listener");
        PreferencesManager.setString(ctx, SMSReceivedBroadcastReceiver.SERVICE_CLASS_PREFERENCES_KEY, service.getName());
    }
}
