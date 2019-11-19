package com.gruppo4.RingApplication.ringCommands;

import com.gruppo4.sms.dataLink.SMSPeer;

/**
 * Class that encapsulates the RingCommand
 *
 * @author Alberto Ursino, Luca Crema
 * <p>
 * Code reviewed by Bortoletti and Barca
 */

public class RingCommand {

    private SMSPeer peer;
    private String password;

    /**
     * Constructor of the class
     *
     * @param peer     sender/receiver of the ring command
     * @param password of the ring command
     */
    public RingCommand(SMSPeer peer, String password) {
        this.peer = peer;
        this.password = password;
    }

    /**
     * @return a SMSPeer object
     */
    public SMSPeer getPeer() {
        return peer;
    }

    /**
     * @return a password
     */
    public String getPassword() {
        return password;
    }

}
