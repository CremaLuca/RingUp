package com.gruppo4.sms.network;

import com.gruppo4.communication.dataLink.listeners.ReceivedMessageListener;
import com.gruppo4.sms.dataLink.SMSMessage;

import java.util.Arrays;

/**
 * This listener receives messages from the broadcast receiver and looks for messages forwarded by
 * the network. It is abstract since an actual implementation requires an instance of SMSNetworkManager,
 * which is abstract (see the class for further explanation).
 * @author Marco Mariotto
 */

public abstract class SMSAbstractNetworkListener implements ReceivedMessageListener<SMSMessage> {

    /*
     * This listener needs an instance of manager in order to let it process incoming requests.
     * JOIN_PROPOSAL requests are handled by the application overriding onJoinProposal().
     * Other requests are handled by the manager, such as adding a user.
     * When we will deal with multiple networks this listener will need a manager for each network.
     *
     */

    protected SMSAbstractNetworkManager manager;

    /*
     * SMS REQUESTS FORMATS
     * Join proposal:    "JP_%netName"
     * Add user:         "AU_%(peer)"          we include the whole peer, not only his address
     * Remove user:      "RU_%(address)"       address is the phone number of the user being removed
     * Add resource:     "AR_%(key)_%(value)"  we include the whole resource, key and value
     * Remove resource:  "RR_%(key)"           we only need the key to identify a resource
     * Don't spread:     "%(1)DS_%(2)"         inform the receiver to not spread this info, it's one of the previous formats where %1 != JP
     */

    protected static final String[] REQUESTS = {SMSAbstractNetworkManager.ADD_USER, SMSAbstractNetworkManager.REMOVE_USER,
            SMSAbstractNetworkManager.ADD_RESOURCE, SMSAbstractNetworkManager.REMOVE_RESOURCE, SMSAbstractNetworkManager.DO_NOT_SPREAD,
            SMSAbstractNetworkManager.JOIN_AGREED, SMSAbstractNetworkManager.JOIN_PROPOSAL};

    @Override
    public void onMessageReceived(SMSMessage message) {
        String request = message.getData().split(SMSAbstractNetworkManager.SPLIT_CHAR)[0];
        if(!Arrays.asList(REQUESTS).contains(request)){
            throw new IllegalArgumentException("Unknown request received");
        }
        else if(request.equals(SMSAbstractNetworkManager.JOIN_PROPOSAL))
            onJoinProposal(message);
        else{
            if(manager == null)
                throw new IllegalStateException("Message not expected: a manager has not been assigned for this network message");
            manager.processRequest(message);
        }
    }
    public abstract void onJoinProposal(SMSMessage message);
}

