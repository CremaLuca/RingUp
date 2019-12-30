package com.gruppo4.RingApplication.structure;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.listeners.SMSReceivedServiceListener;
import com.gruppo4.RingApplication.structure.exceptions.IllegalPasswordException;

/**
 * Class used to capture the message and create a ring command
 *
 * @author Alberto Ursino, Luca Crema
 */
public class ReceivedMessageListener extends SMSReceivedServiceListener {

    private RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();
    private Context context;

    /**
     * Constructor which define a context
     *
     * @param context The current application context
     */
    public ReceivedMessageListener(Context context) {
        this.context = context;
    }

    /**
     * Creation of the command upon receipt of the message
     *
     * @param smsMessage A valid SMSMessage object received
     * @throws IllegalPasswordException Exception thrown by the method "onRingCommandReceived" of the class {@link AppManager} when the password received is not valid
     */
    @Override
    public void onMessageReceived(SMSMessage smsMessage) throws IllegalPasswordException {
        Log.d("ReceivedMessage", "Received a message in the service");
        RingCommand ringCommand = RingCommandHandler.getInstance().parseMessage(smsMessage);
        if (ringCommand != null)
            try {
                AppManager.getInstance().onRingCommandReceived(context, ringCommand, ringtoneHandler.getDefaultRingtone(context));
            } catch (IllegalPasswordException e) {
                Toast.makeText(context, smsMessage.getPeer() + " sent you a wrong password", Toast.LENGTH_SHORT).show();
            }
        else
            Log.d("Invalid RingCommand", "The message received is not a valid RingCommand");
    }

}
