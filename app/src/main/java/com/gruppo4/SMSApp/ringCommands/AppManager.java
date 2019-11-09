package com.gruppo4.SMSApp.ringCommands;

import android.content.Context;

/**
 * @author Gruppo 4
 */
public class AppManager {

    /**
     * If the password of the message received is valid then play ringtone
     *
     * @param context     of the application
     * @param ringCommand a ring command not null
     */
    public static void onRingCommandReceived(Context context, RingCommand ringCommand) {
        if (RingHandler.checkPassword(context, ringCommand)) {
            //TODO RingtoneHandler.playRingtone(15);
        }
    }

    /*
    public static void sendCommand(Context ctx, RingCommand command, SMSSentListener listener){
        SMSMessage message = commandToMessage(command);
        SMSManager.getInstance(ctx).sendMessage(message,listener);

    }

    private static SMSMessage commandToMessage(RingCommand command){
        return new SMSMessage(command.getPeer(),RingtoneHandler.parseCommand(command));
    }
*/
}
