package com.gruppo4.RingApplication.ringCommands.Interfaces;

import com.gruppo4.sms.dataLink.SMSPeer;

public interface RingCommandInterface {

    /**
     * @return a SMSPeer object
     */
    SMSPeer getPeer();

    /**
     * @return a password
     */
    String getPassword();

}
