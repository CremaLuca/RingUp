package com.gruppo4.RingApplication.ringCommands.Interfaces;

import android.content.Context;

import com.gruppo4.RingApplication.ringCommands.RingCommand;

public interface RingCommandHandlerInterface {

    /**
     * Verify that the password in the RingCommand is the same as the one in memory
     *
     * @param context     a valid context
     * @param ringCommand a valid RingCommand object
     * @return a boolean: true = passwords are corresponding, false = passwords are NOT corresponding
     */
    boolean checkPassword(Context context, RingCommand ringCommand);

}
