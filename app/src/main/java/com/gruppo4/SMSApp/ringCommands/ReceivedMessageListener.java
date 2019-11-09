package com.gruppo4.SMSApp.ringCommands;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.gruppo4.SMSApp.MainActivity;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;
import com.gruppo4.sms.dataLink.listeners.SMSReceivedListener;

/**
 * Class used to capture the message and create a ring command
 *
 * @author Alberto Ursino && Luca Crema
 */
public class ReceivedMessageListener implements SMSReceivedListener {

    private static Context context;
    private static RingCommand ringCommand;

    /**
     * Set the context
     *
     * @param ctx application context
     */
    public ReceivedMessageListener(Context ctx) {
        context = ctx;
    }

    /**
     * Creation of the command upon receipt of the message
     *
     * @param message received
     */
    @Override
    public void onMessageReceived(SMSMessage message) {
        Log.d("1", "Il messaggio è arrivato");
        ringCommand = RingHandler.parseContent(message.getPeer(), message.getData());
        if (ringCommand != null)
            AppManager.onRingCommandReceived(context, ringCommand);
        else
            Log.d("Fail: ", "Message received is not a valid command for play the ringtone");
    }
}
