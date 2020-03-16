package com.gruppo4.ringUp.structure;

import android.util.Log;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;

/**
 * Class used to parse RingCommand to SMSMessage and back.
 *
 * @author Alberto Ursino
 * @author Luca Crema
 * @author Marco Mariotto
 */
public class RingCommandHandler {

    public static final String SIGNATURE = "RingUp password: ";
    /**
     * Used in logs
     */
    public static final String CLASS_TAG = "RingCommandHandler";

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
     * A valid content is the following: "ringUp password: {@code password}"
     *
     * @param smsMessage to parse
     * @return a RingCommand object, null if the message doesn't contain a valid one
     */
    public RingCommand parseMessage(@NonNull final SMSMessage smsMessage) {
        String smsMessageData = smsMessage.getData();
        Log.d(CLASS_TAG, "Message received: " + smsMessageData);

        //Control if the smsMessage received contains at least 17 character (SIGNATURE length)
        if (!(smsMessageData.length() > SIGNATURE.length())) {
            Log.d(CLASS_TAG, "The smsMessage received is not long enough, it can't be a right ring command");
            return null;
        }

        String possibleSignature = smsMessageData.substring(0, SIGNATURE.length());
        if (possibleSignature.equals(SIGNATURE)) {
            return new RingCommand(smsMessage.getPeer(), smsMessageData.substring(17));
        }

        Log.d(CLASS_TAG, "The ring command received does not contain the right signature");
        return null;

    }

    /**
     * Extracts the password and the peer from the RingCommand and creates a SMSMessage object with e {@link RingCommandHandler#SIGNATURE} in front
     *
     * @param ringCommand to parse, it must be a valid one
     * @return a SMSMessage object
     * @throws InvalidSMSMessageException      thrown when an SMSMessage received invalid params
     * @throws InvalidTelephoneNumberException thrown when the phone number is not valid
     */
    public SMSMessage parseCommand(@NonNull final RingCommand ringCommand) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        return new SMSMessage(ringCommand.getPeer(), SIGNATURE + ringCommand.getPassword());
    }

}
