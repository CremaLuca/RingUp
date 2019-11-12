package com.gruppo4.RingApplication.ringCommands;

import android.content.Context;
import android.media.Ringtone;
import android.util.Log;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;

/**
 * Class used to capture the message and create a ring command
 *
 * @author Alberto Ursino, Luca Crema
 */
public class ReceivedMessageListener implements SMSReceivedListener {

    private static Context context;
    private static Ringtone ringtone;

    /**
     * Set the context
     *
     * @param context application context
     */
    public ReceivedMessageListener(Context context, Ringtone ringtone) {
        this.context = context;
        this.ringtone = ringtone;
    }

    /**
     * Creation of the command upon receipt of the message
     *
     * @param message received
     */
    @Override
    public void onMessageReceived(SMSMessage message) {
        RingCommand ringCommand = RingCommandHandler.parseContent(message.getPeer(), message.getData());
        if (ringCommand != null)
            AppManager.onRingCommandReceived(context, ringCommand, ringtone);
        else
            Log.d("Fail: ", "Message received is not a valid command for play the ringtone");
    }
}
