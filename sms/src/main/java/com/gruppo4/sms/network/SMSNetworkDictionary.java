package com.gruppo4.sms.network;

import com.gruppo4.communication.network.NetworkDictionary;
import com.gruppo4.sms.dataLink.SMSPeer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SMSNetworkDictionary implements NetworkDictionary<SMSPeer, SMSResource> {

    private ArrayList<SMSPeer> userList = new ArrayList<>();
    private HashMap<SMSPeer, ArrayList<SMSResource>> resourcesDict = new HashMap<>();

    @Override
    public void addUser(SMSPeer peer) {
        userList.add(peer);
    }

    @Override
    public void addResource(SMSPeer user, SMSResource resource) {
        ArrayList<SMSResource> userResources = resourcesDict.get(user);
        if (userResources == null)
            resourcesDict.put(user, new ArrayList<>(Arrays.asList(resource)));
        else {
            userResources.add(resource);
            resourcesDict.put(user, userResources);
        }
    }

    @Override
    public void removeUser(SMSPeer peer) {
        userList.remove(peer);
    }

    @Override
    public void removeResource(SMSPeer user, SMSResource resource) {
        ArrayList<SMSResource> userResources = resourcesDict.get(user);
        if (userResources != null) {
            userResources.remove(resource);
            resourcesDict.put(user, userResources);
        }
    }

    @Override
    public SMSPeer[] getUsersByResource(SMSResource resource) {
        //TODO : Research in array and return the array of occurrences
        return null;
    }

    @Override
    public SMSResource[] getResourcesByUser(SMSPeer user) {
        return (SMSResource[]) resourcesDict.get(user).toArray();
    }

    @Override
    public SMSPeer[] getAllUsers() {
        return (SMSPeer[]) userList.toArray();
    }

    @Override
    public SMSResource[] getAllResources() {
        return (SMSResource[]) resourcesDict.values().toArray();
    }
}
