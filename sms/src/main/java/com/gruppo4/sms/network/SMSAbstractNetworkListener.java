package com.gruppo4.sms.network;

import com.gruppo4.communication.dataLink.listeners.ReceivedMessageListener;
import com.gruppo4.sms.dataLink.SMSMessage;

public abstract class SMSAbstractNetworkListener implements ReceivedMessageListener<SMSMessage> {

    private SMSAbstractNetworkManager manager; //should be an array when we deal with multiple networks

    static final String JOIN_PROPOSAL = "JP";

    @Override
    public void onMessageReceived(SMSMessage message) {
        if(message.getData().equals(JOIN_PROPOSAL)){
            onJoinProposal(message); //onJoinProposal() MUST construct the correct SMSNetwork manager for the specific app
        }
        else
            manager.processRequest(message);
    }
    public abstract void onJoinProposal(SMSMessage message);
}

