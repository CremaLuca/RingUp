package com.gruppo4.sms.network;

import com.gruppo4.communication.network.NetworkManager;
import com.gruppo4.communication.network.SerializableObject;
import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;

import java.util.ArrayList;


/* SMS REQUESTS FORMATS
*  Join proposal:   "JP_%netName"
*  Add user:         "AU_%(peer)"          we include the whole peer, not only his address
*  Remove user:      "RU_%(address)"       address is the phone number of the user being removed
*  Add resource:     "AR_%(key)_%(value)"  we include the whole resource, key and value
*  Remove resource:  "RR_%(key)"           we only need the key to identify a resource
*  Don't spread:     "%(1)DS_%(2)"         inform the receiver to not spread this info, it's one of the previous formats where %1 != JP
* */


public abstract class SMSAbstractNetworkManager implements NetworkManager<SMSPeer, SMSMessage, SerializableObject, SerializableObject>
{
    static final String ADD_USER = "AU";
    static final String REMOVE_USER = "RU";
    static final String ADD_RESOURCE = "AR";
    static final String REMOVE_RESOURCE = "RR";
    static final String DO_NOT_SPREAD = "DS";
    static final String JOIN_AGREED = "JA";

    private SMSNetworkDictionary dict = new SMSNetworkDictionary<>();
    private ArrayList<SMSPeer> joinSent = new ArrayList<>();
    String networkName;
    SMSPeer mySelf;

    private SMSHandler handler;

    public void setup(SMSHandler handler, String networkName, SMSPeer mySelf){
        this.handler = handler;
        this.networkName = networkName;
        this.mySelf = mySelf;
        dict.addUser(mySelf);
    }

    public void invite(SMSPeer peer){
        SMSMessage invMsg = new SMSMessage(handler.getApplicationCode(), peer, SMSAbstractNetworkListener.JOIN_PROPOSAL + "_" + networkName);
        joinSent.add(peer);
        handler.sendMessage(invMsg);
    }
    public void disconnect(){
        spread(REMOVE_USER + "_" + mySelf.getAddress());
        dict = null;
        joinSent = null;
        handler = null;
    }

    public void setResource(SerializableObject key, SerializableObject value){
        dict.setResource(key, value);
        spread(ADD_RESOURCE + "_" + key.toString() + "_" + value.toString());
    }

    public void removeResource(SerializableObject key){
        dict.removeResource(key);
        spread(REMOVE_RESOURCE + "_" + key.toString());
    }

    private void spread(String text){
        ArrayList<SMSPeer> users = dict.getAllUsers();
        int first = users.indexOf(mySelf) + 1;
        for(int i = first; i < users.size() && i < first + 2; i++){
            SMSMessage msg = new SMSMessage(handler.getApplicationCode(), users.get(i), text);
            handler.sendMessage(msg);
        }
    }

    void processRequest(SMSMessage message){
        String text = message.getData();
        SMSPeer sourcePeer = message.getPeer();

        if(text.equals(JOIN_AGREED)){
            if(joinSent.contains(sourcePeer)){
                spread(ADD_USER + "_" + sourcePeer.toString());
                dict.addUser(sourcePeer);
                joinSent.remove(sourcePeer);
                for(Object obj: dict.getKeys()){
                    SerializableObject key = (SerializableObject) obj;
                    SMSMessage record = new SMSMessage(handler.getApplicationCode(), sourcePeer,
                            ADD_RESOURCE + DO_NOT_SPREAD + "_" + key.toString() + "_" + dict.getValue(key).toString());
                    handler.sendMessage(record);
                }
                for(Object obj: dict.getAllUsers()){
                    SMSPeer peer = (SMSPeer) obj;
                    SMSMessage record = new SMSMessage(handler.getApplicationCode(), sourcePeer,
                            ADD_USER + DO_NOT_SPREAD + "_" + peer.toString());
                    handler.sendMessage(record);
                }
            }
            else{
                //ignore
            }
        }
        else if(text.equals(ADD_USER)){
            SMSPeer p = new SMSPeer(text.split("_")[1]);
            spread(ADD_USER + "_" + p.toString());
            dict.addUser(p);
        }
        else if(text.equals(ADD_USER + DO_NOT_SPREAD)){
            SMSPeer p = new SMSPeer(text.split("_")[1]);
            dict.addUser(p);
        }
        else if(text.equals(REMOVE_USER)){
            SMSPeer p = new SMSPeer(text.split("_")[1]);
            spread(REMOVE_USER + "_" + p.getAddress());
            dict.removeUser(p);
        }
        else if(text.equals(ADD_RESOURCE)){
            String[] args = text.split("_");
            spread(ADD_RESOURCE + "_" + args[1] + "_" + args[2]);
            dict.setResource(getKeyFromString(args[1]), getValueFromString(args[2]));
        }
        else if(text.equals(ADD_RESOURCE + DO_NOT_SPREAD)){
            String[] args = text.split("_");
            dict.setResource(getKeyFromString(args[1]), getValueFromString(args[2]));
        }
        else if(text.equals(REMOVE_RESOURCE)){
            String key = text.split("_")[1];
            spread(REMOVE_RESOURCE + "_" + key);
            dict.removeResource(getKeyFromString(key));
        }
        else{
            throw new IllegalStateException("Should not have received this command");
        }
    }

    public abstract SerializableObject getKeyFromString(String key);
    public abstract SerializableObject getValueFromString(String value);
}
