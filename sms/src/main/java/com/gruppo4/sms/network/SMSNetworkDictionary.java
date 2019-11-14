package com.gruppo4.sms.network;


import com.gruppo4.communication.network.NetworkDictionary;
import com.gruppo4.sms.dataLink.SMSPeer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Network dictionary for peers and resources on the SMS network
 *
 * @author Luca Crema, Marco Mariotto
 */
public class SMSNetworkDictionary<RK, RV> implements NetworkDictionary<SMSPeer, RK, RV> {

    /**
     * Sorted user list
     */
    private ArrayList<SMSPeer> userList;

    private HashMap<RK, RV> resourcesDict;

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
    public void addUser(SMSPeer peer) {
        if (userList.contains(peer))
            return;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).compareTo(peer) > 0) {
                userList.add(i, peer);
                return;
            }
        }
        //If the array is empty or the position to insert the user is the last one
        userList.add(peer);
    }

    /**
     * @return a list of all current users in the network
     */
    @Override
    public ArrayList<SMSPeer> getAllUsers() {
        return userList;
    }

    /**
     * Removes a SMS user from the network
     *
     * @param peer SMS user
     */
    @Override
    public void removeUser(SMSPeer peer) {
        userList.remove(peer);
    }

    /**
     * Sets the value of the key to the resource value
     *
     * @param key   identification for the resource
     * @param value of the resource
     * @return the previous resource value if there was one, null otherwise
     */
    @Override
    public RV setResource(RK key, RV value) {
        return resourcesDict.put(key, value);
    }

    /**
     * Remove the resource from the network
     *
     * @param resourceKey identification for the resource
     * @return the removed resource value
     */
    @Override
    public RV removeResource(RK resourceKey) {
        return resourcesDict.remove(resourceKey);
    }

    /**
     * Get the value given a key
     *
     * @param resourceKey identification for the resource
     * @return the requested value
     */
    @Override
    public RV getValue(RK resourceKey) {
        return resourcesDict.get(resourceKey);
    }

    /**
     * Retrieve all keys
     * @return {@link ArrayList} of keys in the dictionary
     */
    @Override
    public ArrayList<RK> getAllKeys() {
        return new ArrayList<>(resourcesDict.keySet());
    }

    /**
     * Retrieve all values
     * @return {@link ArrayList} of values in the dictionary
     */
    @Override
    public ArrayList<RV> getAllValues() {
        return new ArrayList<>(resourcesDict.values());
    }
}
