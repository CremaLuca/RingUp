package com.gruppo4.sms.network;

import com.gruppo4.communication.network.NetworkManager;
import com.gruppo4.communication.network.SerializableObject;
import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;

import java.util.ArrayList;

/**
 * This class is intended to be extended by the specific application. It is an implementation of NetworkManager.
 * Each SMS request has a format specified below. A node cannot inform each node in the network about his changes to
 * the local dictionary, for it will be expensive in terms of messages sent. Thus we ordered our dictionary by phone number, so that
 * each node has the same dictionary ordered in the same way: a node with index n in the dictionary will spread changes to only nodes numbered
 * 2n+1, 2n+2, guaranteeing mutual exclusion.
 * This costs 2 messages for each node and delivery of information is logarithmic in N, the extension of the network.
 *
 * @author Marco Mariotto
 */

public abstract class SMSAbstractNetworkManager implements NetworkManager<SMSPeer, SerializableObject, SerializableObject>
{
    /*
     * SMS REQUESTS FORMATS
     * Join proposal:    "JP_%netName"
     * Add user:         "AU_%(peer)"          we include the whole peer, not only his address
     * Remove user:      "RU_%(address)"       address is the phone number of the user being removed
     * Add resource:     "AR_%(key)_%(value)"  we include the whole resource, key and value
     * Remove resource:  "RR_%(key)"           we only need the key to identify a resource
     * Don't spread:     "%(1)DS_%(2)"         inform the receiver to not spread this info, it's one of the previous formats where %1 != JP
     */

    static final String ADD_USER = "AU";
    static final String REMOVE_USER = "RU";
    static final String ADD_RESOURCE = "AR";
    static final String REMOVE_RESOURCE = "RR";
    static final String DO_NOT_SPREAD = "DS";
    static final String JOIN_AGREED = "JA";
    static final String JOIN_PROPOSAL = "JP";
    static final String SPLIT_CHAR = "_";

    private SMSNetworkDictionary dict = new SMSNetworkDictionary<>();

    // joinSent keeps track of JOIN_PROPOSAL requests still pending.
    private ArrayList<SMSPeer> joinSent = new ArrayList<>();
    protected  String networkName;
    // mySelf is the current peer setting up the network
    protected  SMSPeer mySelf;

    //manager makes use of SMSHandler to send requests
    private SMSHandler handler;

    /**
     * Set up a new network
     *
     * @param handler we set up a handler for sending requests
     * @param networkName of the network being created
     */
    public void setup(SMSHandler handler, String networkName, SMSPeer mySelf){
        this.handler = handler;
        this.networkName = networkName;
        this.mySelf = mySelf;
        dict.addUser(mySelf);
    }

    /**
     * Sends an invitation (JOIN_PROPOSAL request) to the specified peer
     *
     * @param peer who is asked to join the network
     */
    public void invite(SMSPeer peer){
        SMSMessage invMsg = new SMSMessage(handler.getApplicationCode(), peer, JOIN_PROPOSAL + "_" + networkName);
        joinSent.add(peer);
        handler.sendMessage(invMsg);
    }

    /**
     * Inform every user in the network that the current application is disconnecting from the network
     * Sends a REMOVE_USER request, where user is mySelf
     */

    public void disconnect(){
        spread(REMOVE_USER + "_" + mySelf.getAddress());
        dict = null;
        joinSent = null;
        handler = null;
    }

    /**
     * set a key-value resource for the local dictionary and spread this information to every user
     *
     * @param key resource key
     * @param value resource value
     */

    public void setResource(SerializableObject key, SerializableObject value){
        dict.setResource(key, value);
        spread(ADD_RESOURCE + "_" + key.toString() + "_" + value.toString());
    }

    /**
     * remove a key-value resource from the local dictionary and spread this information to every user
     *
     * @param key resource key
     */

    public void removeResource(SerializableObject key){
        dict.removeResource(key);
        spread(REMOVE_RESOURCE + "_" + key.toString());
    }

    /**
     * Spreads this string according to your current index n in the dictionary. We send text to nodes with index
     * 2n+1, 2n+2.
     *
     * @param text to be sent
     */

    private void spread(String text){
        ArrayList<SMSPeer> users = dict.getAllUsers();
        int first = 2*users.indexOf(mySelf) + 1;
        for(int i = first; i < users.size() && i < first + 2; i++){
            SMSMessage msg = new SMSMessage(handler.getApplicationCode(), users.get(i), text);
            handler.sendMessage(msg);
        }
    }

    /**
     * It process every request performing changes to the local dictionary. If otherwise specified (DO_NOT_SPREAD flag included)
     * spreads each request to his corresponding nodes according to the number rule previously stated.
     * When a JOIN_AGREED request has to be processed, we also send the whole dictionary to the new node who just joined.
     *
     * @param message containing the request to be processed
     */

    void processRequest(SMSMessage message){
        String text = message.getData();
        SMSPeer sourcePeer = message.getPeer();

        if(text.equals(JOIN_AGREED)){
            if(joinSent.contains(sourcePeer)){
                spread(ADD_USER + "_" + sourcePeer.toString());
                dict.addUser(sourcePeer);
                joinSent.remove(sourcePeer);
                //send the whole dictionary to sourcePeer
                for(Object obj: dict.getKeys()){
                    SerializableObject key = (SerializableObject) obj;
                    SMSMessage record = new SMSMessage(handler.getApplicationCode(), sourcePeer,
                            ADD_RESOURCE + DO_NOT_SPREAD +
                                    "_" + key.toString() + "_" + dict.getValue(key).toString());
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
                //ignore non-matching joins
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

    /**
     * Construction of specific objects for resource keys cannot be done here. It is up to the application to override this method.
     *
     * @param key as string
     */

    protected abstract SerializableObject getKeyFromString(String key);

    /**
     * Construction of specific objects for resource values cannot be done here. It is up to the application to override this method.
     *
     * @param value as string
     */
    protected abstract SerializableObject getValueFromString(String value);
}
