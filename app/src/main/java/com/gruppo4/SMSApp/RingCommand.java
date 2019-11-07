package com.gruppo4.SMSApp;

import com.gruppo4.sms.dataLink.SMSPeer;

public class RingCommand {

    private SMSPeer sender;
    private String password;

    /**
     * Constructor of the class
     *
     * @param sender   the peer who sent the command
     * @param password the password passed by the sender
     */
    protected RingCommand(SMSPeer sender, String password) {
        this.sender = sender;
        this.password = password;
    }

    /**
     * Return the sender
     *
     * @return a SMSPeer object
     */
    public SMSPeer getSender() {
        return sender;
    }

    /**
     * Return the password
     *
     * @return a password
     */
    public String getPassword() {
        return password;
    }

}
