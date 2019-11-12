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
    private final String JOIN_REQUEST = "JOIN-REQ";
    private final String LEAVE_REQUEST = "LEAVE-REQ";
    private final String ADD_USER = "ADD-USER";
    private final String REMOVE_USER = "RMV-USER";
    private final String ADD_RESOURCE = "ADD-RES";
    private final String REMOVE_RESOURCE = "RMV-RES";
    private final String JOIN_ACCEPT = "JOIN-ACC";

    private static SMSNetworkDictionary dict = new SMSNetworkDictionary();
    private static SMSNetworkManager instance;
    private static SMSHandler handler;
    private static String networkName;
    private static ArrayList<SMSPeer> joinSent = new ArrayList<>();


    static public SMSNetworkManager getInstance(Context ctx){
        if (instance == null){
            instance = new SMSNetworkManager();
            handler = SMSHandler.getInstance(ctx);
        }
        return instance;
    }

    public void setup(int applicationCode, String networkName){
        handler.setup(applicationCode);
        SMSNetworkManager.networkName = networkName;
    }

    public void invite(SMSPeer peer){
        SMSMessage inv = new SMSMessage(handler.getApplicationCode(), peer, JOIN_REQUEST + "_" + networkName);
        joinSent.add(peer);
        handler.sendMessage(inv);
    }
    public  void disconnect(){

    }
    public void addResource(Resource res){

    }
    public void removeResource(Resource res){

    }

    public void onMessageReceived(SMSMessage message){
        //we update dict according to requests type

    }
}
