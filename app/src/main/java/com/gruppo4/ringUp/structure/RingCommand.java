package com.gruppo4.ringUp.structure;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;

/**
 * Class that encapsulates the peer and the password of a ring command.
 *
 * @author Alberto Ursino
 * @author Luca Crema
 */
public class RingCommand {

    private SMSPeer peer;
    private String password;

    /**
     * Constructor of the class.
     *
     * @param peer     sender/receiver of the ring command
     * @param password of the ring command
     */
    public RingCommand(@NonNull final SMSPeer peer, @NonNull final String password) {
        this.peer = peer;
        this.password = password;
    }

    /**
     * @return who sent the command.
     */
    public SMSPeer getPeer() {
        return peer;
    }

    /**
     * @return password contained in the command.
     */
    public String getPassword() {
        return password;
    }

}

