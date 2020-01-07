package com.gruppo4.ringUp.structure;

import android.util.Log;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;

/**
 * Class used to parse RingCommand to SMSMessage and back
 *
 * @author Alberto Ursino, Luca Crema, Marco Mariotto
 */
public class RingCommandHandler {

    public static final String SIGNATURE = "ringUp password: ";

    /**
     * Instance of the class that is instantiated in getInstance method
     */
    private static RingCommandHandler instance = null;

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
     * A valid content is the following: "ringUp password: password"
     *
     * @param smsMessage to parse
     * @return a RingCommand object, null if the message doesn't contain a valid one
     */
    public RingCommand parseMessage(SMSMessage smsMessage) {
        String smsMessageData = smsMessage.getData();
        Log.d("RingCommandHandler", "Message arrived: " + smsMessageData);
        //Control if the smsMessage received contains at least 17 character (SIGNATURE length)
        if (!(smsMessageData.length() > SIGNATURE.length())) {
            Log.d("RingCommandHandler", "The smsMessage received is not long enough, it can't be a right ring command");
            return null;
        } else {
            String possibleSignature = smsMessageData.substring(0, SIGNATURE.length());
            if (possibleSignature.equals(SIGNATURE)) {
                return new RingCommand(smsMessage.getPeer(), smsMessageData.substring(17));
            } else {
                Log.d("RingCommandHandler", "The ring command received doesn't contain the right signature");
                return null;
            }
        }
    }

    /**
     * Extracts the password and the peer from the RingCommand and creates a SMSMessage object
     *
     * @param ringCommand to parse, it must be a valid one
     * @return a SMSMessage object
     * @throws InvalidSMSMessageException      thrown when an SMSMessage received invalid params
     * @throws InvalidTelephoneNumberException thrown when the phone number is not valid
     */
    public SMSMessage parseCommand(RingCommand ringCommand) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        return new SMSMessage(ringCommand.getPeer(), ringCommand.getPassword());
    }

}
