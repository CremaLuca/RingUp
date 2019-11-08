package com.gruppo4.communication.network;

import com.gruppo4.communication.dataLink.Peer;

/**
 * @param <KU> Key for user
 * @param <KR> Key for resource
 * @param <P>  Peer for user
 * @param <V>  Values for resource
 */
public interface NetworkDictionary<KU, KR, P extends Peer, V> {

    void addUser(KU key, P peer);

    void addResource(KR key, V value);

    void removeUser(KU key);

    void removeResource(KR key);

    P getUser(KU key);

    V getResource(KR key);

    P[] getAllUser();

    V[] getAllResources();
}
