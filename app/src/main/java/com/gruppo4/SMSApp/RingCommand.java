package com.gruppo4.SMSApp;

import com.gruppo4.sms.dataLink.SMSPeer;

/**
 * Class that encapsulates the RingCommand
 *
 * @author Alberto Ursino
 *
 * revised by Luca Crema
 */

public class RingCommand {

    private SMSPeer peer;
    private String password;

    /**
     * Constructor of the class
     *
     * @param peer     sender/receiver of the RingCommand
     * @param password a valid password
     */
    protected RingCommand(SMSPeer peer, String password) {
        this.peer = peer;
        this.password = password;
    }

    /**
     * @return a SMSPeer object
     */
    public SMSPeer getSender() {
        return peer;
    }

    /**
     * @return a password
     */
    public String getPassword() {
        return password;
    }

}
