package com.gruppo4.RingApplication.structure;

import android.content.Context;

import com.eis.smslibrary.SMSMessage;

/**
 * Class used to parse RingCommand to SMSMessage and back
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
     * Extracts the password from the message received and create a RingCommand
     * A valid content is the following: "_password"
     *
     * @param smsMessage to parse
     * @return a RingCommand object, null if the message doesn't contain a valid one
     */
    public RingCommand parseMessage(SMSMessage smsMessage) {
        if ((smsMessage.getData().charAt(0) + "").equals(SPLIT_CHARACTER)) {
            String[] parts = smsMessage.getData().split(SPLIT_CHARACTER);
            //parts[0] is empty, parts[1] contains the password
            return new RingCommand(smsMessage.getPeer(), parts[1]);
        }
        return null;
    }

    /**
     * Extracts the password and the peer from the RingCommand and creates a SMSMessage object
     *
     * @param ringCommand to parse, it must be a valid one
     * @param context     of the application
     * @return a SMSMessage object
     */
    public SMSMessage parseCommand(Context context, RingCommand ringCommand) {
        return new SMSMessage(ringCommand.getPeer(), ringCommand.getPassword());
    }
}
