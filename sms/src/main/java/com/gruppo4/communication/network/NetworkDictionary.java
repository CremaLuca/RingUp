package com.gruppo4.communication.network;

import com.gruppo4.communication.dataLink.Peer;

/**
 * @param <U>  Peer for user
 * @param <R>  Values for resource
 * @author Group 4
 */
public interface NetworkDictionary<U extends Peer, R extends Resource> {

    /**
     * Registers a user to the network
     *
     * @param user
     */
    void addUser(U user);

    /**
     * Adds a resource to the network
     *
     * @param user
     * @param resource
     */
    void addResource(U user, R resource);

    /**
     * Removes the user from the network
     *
     * @param user
     */
    void removeUser(U user);

    /**
     * Removes a resource availability from the network
     *
     * @param user
     * @param resource
     */
    void removeResource(U user, R resource);

    /**
     * Get all the users that has that resource
     *
     * @param resource
     * @return
     */
    U[] getUsersByResource(R resource);

    /**
     * Get all the resources for a user
     * @param user
     * @return
     */
    R[] getResourcesByUser(U user);

    /**
     * Get the whole list of users from the network
     * @return a list of users in the network
     */
    U[] getAllUsers();

    /**
     * Get all the resources available in the network
     * @return a list of resources in the network
     */
    R[] getAllResources();
}
