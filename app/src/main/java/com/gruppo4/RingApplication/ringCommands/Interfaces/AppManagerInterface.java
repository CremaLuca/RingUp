package com.gruppo4.RingApplication.ringCommands.Interfaces;

import android.content.Context;
import android.media.Ringtone;

import com.gruppo4.RingApplication.ringCommands.RingCommand;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;
import com.gruppo4.sms.dataLink.listeners.SMSSentListener;

public interface AppManagerInterface {

    /**
     * If the password of the message received is valid then play ringtone for fixed amount of time
     *
     * @param context     of the application
     * @param ringCommand a ring command not null
     */
    void onRingCommandReceived(Context context, RingCommand ringCommand, final Ringtone ringtone);

    /**
     * Calls the class SMS Handler and passed to it an SMS Message object, built with the ring command
     *
     * @param context     of the application
     * @param ringCommand to send
     * @param listener
     * @throws InvalidTelephoneNumberException
     * @throws InvalidSMSMessageException
     */
    void sendCommand(Context context, RingCommand ringCommand, SMSSentListener listener) throws InvalidSMSMessageException, InvalidTelephoneNumberException;

}
