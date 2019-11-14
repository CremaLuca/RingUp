package com.gruppo4.communication.network;

import com.gruppo4.communication.dataLink.Peer;

import java.util.ArrayList;

/**
 * @param <U> Peer for users in the network
 * @author Group 4
 */
public interface NetworkDictionary<U extends Peer, RK, RV> {

    /**
     * Registers a user to the network
     *
     * @param user new network user
     */
    void addUser(U user);

    /**
     * Get the whole list of users from the network
     *
     * @return a list of users in the network
     */
    ArrayList<U> getAllUsers();

    /**
     * Removes the user from the network
     *
     * @param user registered user
     */
    void removeUser(U user);

    /**
     * Sets the value of the key to the resource value
     *
     * @param key   a resource key present in the dictionary
     * @param value updated value of the resource
     * @return the previous resource value if there was one, null otherwise
     */
    RV setResource(RK key, RV value);

    /**
     * Removes a resource availability from the network
     *
     * @param resourceKey identification for the resource
     * @return the removed resource value
     */
    RV removeResource(RK resourceKey);

    /**
     * Get the value given a key
     *
     * @param resourceKey identification for the resource
     * @return requested resource if present
     */
    RV getValue(RK resourceKey);

    /**
     * Get the whole list of users from the network
     *
     * @return a list of users in the network
     */
    ArrayList<RK> getAllKeys();

    /**
     * Get all the resources available in the network
     *
     * @return a list of resources in the network
     */
    ArrayList<RV> getAllValues();
}
