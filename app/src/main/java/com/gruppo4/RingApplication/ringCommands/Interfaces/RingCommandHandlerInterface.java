package com.gruppo4.RingApplication.ringCommands.Interfaces;

import com.gruppo4.RingApplication.ringCommands.RingCommand;
import com.gruppo4.sms.dataLink.SMSPeer;

public interface RingCommandHandlerInterface {

    /**
     * Extracts the password from the message received and create a RingCommand, null if the command is not valid
     *
     * @param peer    is the sender/receiver of the message
     * @param content command
     * @return a RingCommand
     */
    RingCommand parseContent(SMSPeer peer, String content);

}
