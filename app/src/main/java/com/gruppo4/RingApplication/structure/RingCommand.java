package com.gruppo4.RingApplication.structure;

import com.eis.smslibrary.SMSPeer;

/**
 * Class that encapsulates the peer and the password of a ring command
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
     * @return the RingCommand peer
     */
    public SMSPeer getPeer() {
        return peer;
    }

    /**
     * @return the RingCommand password
     */
    public String getPassword() {
        return password;
    }

}
