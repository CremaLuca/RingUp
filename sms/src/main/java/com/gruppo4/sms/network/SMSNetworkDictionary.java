package com.gruppo4.sms.network;

import com.gruppo4.communication.dataLink.Peer;
import com.gruppo4.communication.network.NetworkDictionary;
import com.gruppo4.communication.network.Resource;
import com.gruppo4.sms.dataLink.SMSPeer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

/**
 * Network dictionary for peers and resources on the SMS network
 *
 * @author Luca Crema
 */
public class SMSNetworkDictionary implements NetworkDictionary<SMSPeer> {

    private ArrayList<SMSPeer> userList;
    private HashMap<SMSPeer, Set<Resource>> resourcesDict;

    /**
     * Constructor for the dictionary
     */
    protected SMSNetworkDictionary() {
        userList = new ArrayList<>();
        resourcesDict = new HashMap<>();
    }

    /**
     * Registers a SMS user to the network
     *
     * @param peer SMS user
     */
    @Override
    public void addUser(SMSPeer peer) { //dict is sorted by address
        for(int i = 0; i < userList.size(); i++){
            if(Integer.parseInt(userList.get(i).getAddress()) > Integer.parseInt(peer.getAddress())){
                userList.add(i, peer);
                break;
            }
        }
    }

    /**
     * Registers the couple key value (user, resource) into the dictionary
     * The user must not have already another copy of the resource
     *
     * @param user     the holder of the resource, must be already in the user list via {@link #addUser(SMSPeer) addUser}
     * @param resource the resource identification and details
     */
    @Override
    public void addResource(SMSPeer user, Resource resource) {
        Set<Resource> userResources = resourcesDict.get(user);
        if (userResources == null)
            resourcesDict.put(user, new HashSet<>(Arrays.asList(resource)));
        else {
            userResources.add(resource);
            resourcesDict.put(user, userResources);
        }
    }

    /**
     * Removes a SMS user from the network
     *
     * @param peer SMS user
     */
    @Override
    public void removeUser(SMSPeer peer) {
        userList.remove(peer);
        resourcesDict.remove(peer);
    }

    /**
     * Removes a resource availability from the network dictionary
     *
     * @param user     SMS user that had the resource
     * @param resource resource no more available
     */
    @Override
    public void removeResource(SMSPeer user, Resource resource) {
        Set<Resource> userResources = resourcesDict.get(user);
        if (userResources != null) {
            userResources.remove(resource);
            resourcesDict.put(user, userResources);
        }
    }

    /**
     * Finds all the SMS network users that have that resource
     *
     * @param resource wanted resource
     * @return array of SMS users
     */
    @Override
    public ArrayList<SMSPeer> getUsersByResource(Resource resource) {
        ArrayList<SMSPeer> peers = new ArrayList<>();
        for (SMSPeer user : resourcesDict.keySet()) {
            for (Resource currentResource : resourcesDict.get(user)) {
                if (resource.equals(currentResource)) {
                    peers.add(user);
                    break;//we don't have to look in this user's resources anymore, he can only have one
                }
            }
        }
        return peers;
    }

    /**
     * Retrieves all the resources from a given SMS network user
     *
     * @param user SMS newtork user
     * @return array of resources for the given SMS user
     */
    @Override
    public ArrayList<Resource> getResourcesByUser(SMSPeer user) {
        Set<Resource> userResources = resourcesDict.get(user);
        return new ArrayList<>(userResources);
    }

    /**
     * @return an array of all the users in the network
     */
    @Override
    public ArrayList<SMSPeer> getAllUsers() {
        return userList;
    }

    /**
     * Could contain duplicates because multiple users can have the same resource
     *
     * @return an array of all the resources available in the netwrok
     */
    @Override
    public  ArrayList<Resource> getAllResources() {
        Set<Resource> resourceList = new HashSet<>();
        for (Set<Resource> userResources : resourcesDict.values()) {
            resourceList.addAll(userResources);
        }
        return new ArrayList<>(resourceList);
    }
}
