package com.gruppo4.RingApplication.structure;

import com.gruppo4.sms.dataLink.SMSPeer;

/**
 * Class used to check if password in memory corresponds to the password passed through sender
 *
 * @author Alberto Ursino, Luca Crema, Marco Mariotto
 */

public class RingCommandHandler {

    public static final String SPLIT_CHARACTER = "_";

    /**
     * Instance of the class that is instantiated in getInstance method
     */
    private static RingCommandHandler instance;

    /**
     * Private constructor
     */
    private RingCommandHandler() {
    }

    /**
     * @return the RingCommandHandler instance
     */
    public static RingCommandHandler getInstance() {
        if (instance == null)
            instance = new RingCommandHandler();
        return instance;
    }

    /**
     * Extracts the password from the message received and create a RingCommand, null if the command is not valid
     * A valid content is the following: "_password"
     *
     * @param peer    is the sender/receiver of the message
     * @param content command
     * @return a RingCommand
     */
    public RingCommand parseContent(SMSPeer peer, String content) {
        if ((content.charAt(0) + "").equals(SPLIT_CHARACTER)) {
            String[] parts = content.split(SPLIT_CHARACTER);
            //parts[0] is empty, parts[1] contains the password
            return new RingCommand(peer, parts[1]);
        }
        return null;
    }

}
