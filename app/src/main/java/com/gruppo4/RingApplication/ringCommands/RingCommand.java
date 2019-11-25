package com.gruppo4.RingApplication.ringCommands;

import com.gruppo4.RingApplication.ringCommands.Interfaces.RingCommandInterface;
import com.gruppo4.sms.dataLink.SMSPeer;

/**
 * Class that encapsulates the RingCommand
 *
 * @author Alberto Ursino, Luca Crema
 * <p>
 * Code reviewed by Bortoletti and Barca
 */

public class RingCommand implements RingCommandInterface {

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

    @Override
    public SMSPeer getPeer() {
        return peer;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
