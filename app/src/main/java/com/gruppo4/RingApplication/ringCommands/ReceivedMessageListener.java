package com.gruppo4.RingApplication.ringCommands;

import android.util.Log;

import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;

/**
 * Class used to capture the message and create a ring command
 *
 * @author Alberto Ursino, Luca Crema
 */
public class ReceivedMessageListener extends SMSReceivedListener {

    private RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();

    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ReceivedMessageListener(String name) {
        super(name);
    }

    public ReceivedMessageListener() {
        super("ReceivedMessageListener");
    }

    /**
     * Creation of the command upon receipt of the message
     *
     * @param smsMessage received
     */
    @Override
    public void onMessageReceived(SMSMessage smsMessage) {
        Log.d("ReceivedMessage", "Received a message in the service");
        RingCommand ringCommand = RingCommandHandler.getInstance().parseContent(smsMessage.getPeer(), smsMessage.getData());
        if (ringCommand != null)
            AppManager.getInstance().onRingCommandReceived(this, ringCommand, ringtoneHandler.getDefaultRingtone(this));
        else
            Log.d("Fail: ", "Message received is not a valid command for play the ringtone");
    }
}
