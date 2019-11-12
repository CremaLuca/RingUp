package com.gruppo4.sms.network;

import android.content.Context;

import com.gruppo4.communication.dataLink.Peer;
import com.gruppo4.communication.network.NetworkManager;
import com.gruppo4.communication.network.Resource;
import com.gruppo4.sms.dataLink.SMSHandler;
import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSPeer;

import java.util.ArrayList;


/* SMS REQUESTS FORMATS
*  Join:            "JOIN-REQUEST_%networkName"
*  Leave:           "LEAVE-REQUEST_%address"     //node x send leave requests to K nodes, who will spread this request to other nodes: for this to be done, we pass the address of x
*  AddUser:         "ADD-USER_%peerInfo"             //we include info from peer
*  RemoveUser:      "REMOVE-USER_%address"
*  AddResource:     "ADD-RESOURCE_%resourceInfo"     //we include info from resource
*  RemoveResource:  "REMOVE-RESOURCE_%resourceIdentifier"
* */


public class SMSNetworkManager implements NetworkManager<SMSPeer, Resource, SMSMessage>
{
    private final String JOIN_PROPOSAL = "JOIN-PRO"; //we send
    private final String JOIN_AGREED = "JOIN-ACK"; //newcomer is welcome
    private final String ADD_USER = "ADD-USER"; //someone has connected
    private final String REMOVE_USER = "RMV-USER"; //someone has disconnected
    private final String ADD_RESOURCE = "ADD-RES";
    private final String REMOVE_RESOURCE = "RMV-RES";

    private static SMSNetworkDictionary dict = new SMSNetworkDictionary();
    private static SMSNetworkManager instance;
    private static SMSHandler handler;
    private static String networkName;
    private static SMSPeer mySelf;
    private static ArrayList<SMSPeer> joinSent = new ArrayList<>();

    static public SMSNetworkManager getInstance(Context ctx){
        if (instance == null){
            instance = new SMSNetworkManager();
            handler = SMSHandler.getInstance(ctx);
        }
        return instance;
    }

    public void setup(int applicationCode, String networkName, SMSPeer mySelf){
        handler.setup(applicationCode);
        SMSNetworkManager.networkName = networkName;
        SMSNetworkManager.mySelf = mySelf;
    }

    public void invite(SMSPeer peer){
        SMSMessage invMsg = new SMSMessage(handler.getApplicationCode(), peer, JOIN_PROPOSAL + "_" + networkName);
        joinSent.add(peer);
        handler.sendMessage(invMsg);
    }
    public  void disconnect(){
        spread(REMOVE_USER + "_" + mySelf.toString());
        dict = new SMSNetworkDictionary();
        joinSent = new ArrayList<>();
    }
    public void addResource(Resource res){
        spread(ADD_RESOURCE + "_" + res.toString() + "_" + mySelf.getAddress());
        dict.addResource(mySelf, res);
    }
    public void removeResource(Resource res){
        spread(REMOVE_RESOURCE + "_" + res.toString() + "_" + mySelf.getAddress());
        dict.removeResource(mySelf, res);
    }

    public void addUser(SMSPeer peer){
        spread(ADD_USER + "_" + peer.toString());
        dict.addUser(peer);
    }

    public  void removeUser(SMSPeer peer){
        spread(REMOVE_USER + "_" + peer.toString());
        dict.removeUser(peer);
    }

    public void onMessageReceived(SMSMessage message){
        String text = message.getData();
        SMSPeer sourcePeer = message.getPeer();

        if(text.equals(JOIN_PROPOSAL)){
            //we should let the app decide
        }
        else if(text.equals(JOIN_AGREED)){

            if(joinSent.contains(sourcePeer)){
                addUser(sourcePeer);
                joinSent.remove(sourcePeer);
                //send the whole dict to the newcomer
                for(SMSPeer p: dict.getAllUsers()){
                    SMSMessage msg = new SMSMessage(handler.getApplicationCode(), sourcePeer, ADD_USER + "_" + p.toString());
                    handler.sendMessage(msg);
                }
            }
            else{
                //ignore
            }
        }
        else if(text.equals(ADD_USER)){
            addUser(new SMSPeer(text.split("_")[1]));
        }
        else if(text.equals(REMOVE_USER)){
            removeUser(new SMSPeer(text.split("_")[1]));
        }
        else if(text.equals(ADD_RESOURCE)){
            //should build resource from textMsg
        }
        else if(text.equals(REMOVE_RESOURCE)){
            //should build resource from textMsg
        }
        else{
            throw new IllegalStateException("Should not have received this command");
        }
    }

    private void spread(String text){
        ArrayList<SMSPeer> users = dict.getAllUsers();
        int first = users.indexOf(mySelf) + 1;
        for(int i = first; i < users.size() && i < first + 2; i++){
            SMSMessage msg = new SMSMessage(handler.getApplicationCode(), users.get(i), text);
            handler.sendMessage(msg);
        }
    }
}
