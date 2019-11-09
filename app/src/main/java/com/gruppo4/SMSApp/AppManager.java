package com.gruppo4.SMSApp;

import android.content.Context;

import com.gruppo4.SMSApp.ringCommands.RingCommand;
import com.gruppo4.SMSApp.ringCommands.RingHandler;

public class AppManager {

    public static void onRingCommandReceived(Context ctx, RingCommand command){
        if(RingHandler.checkPassword(ctx, command)){
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
