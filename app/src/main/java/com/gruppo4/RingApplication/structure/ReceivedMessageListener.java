package com.gruppo4.RingApplication.structure;

import android.content.Context;
import android.util.Log;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.listeners.SMSReceivedListener;

/**
 * Class used to capture the message and create a ring command
 *
 * @author Alberto Ursino, Luca Crema
 */
public class ReceivedMessageListener implements SMSReceivedListener {

    private RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();
    private Context context = null;

    /**
     * Constructor which define a context
     *
     * @param context of the application
     */
    public ReceivedMessageListener(Context context) {
        this.context = context;
    }

    /**
     * Creation of the command upon receipt of the message
     *
     * @param smsMessage received
     */
    @Override
    public void onMessageReceived(SMSMessage smsMessage) {
        Log.d("ReceivedMessage", "Received a message in the service");
        RingCommand ringCommand = RingCommandHandler.getInstance().parseMessage(smsMessage);
        if (ringCommand != null)
            AppManager.getInstance().onRingCommandReceived(context, ringCommand, ringtoneHandler.getDefaultRingtone(context));
        else
            Log.d("Fail: ", "Message received is not a valid command for play the ringtone");
    }

}
