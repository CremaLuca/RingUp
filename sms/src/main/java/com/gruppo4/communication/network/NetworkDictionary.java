package com.gruppo4.communication.network;

import com.gruppo4.communication.dataLink.Peer;

/**
 * @param <U>  Peer for user
 * @param <R>  Values for resource
 */
public interface NetworkDictionary<U extends Peer, R> {

    void addUser(U peer);

    void addResource(U user, R value);

    void removeUser(U peer);

    void removeResource(U user);

    U getUserByResource(R resource);

    R getResourcesByUser(U user);

    U[] getAllUsers();

    R[] getAllResources();
}
