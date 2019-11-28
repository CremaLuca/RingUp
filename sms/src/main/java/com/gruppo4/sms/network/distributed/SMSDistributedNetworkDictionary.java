package com.gruppo4.sms.network.distributed;

import com.gruppo4.communication.network.NetworkDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class SMSDistributedNetworkDictionary<RK, RV> implements NetworkDictionary<KADAddress, RK, RV> {

    /**
     * Maximum users per bucket
     */
    public static final int MAX_USER_BUCKET_LENGTH = 5;


    public SMSDistributedNetworkDictionary(KADAddress userAddress) {

    }

    /**
     * Adds a user to the list of
     *
     * @param user new network user
     */
    @Override
    public void addUser(KADAddress user) {

    }

    /**
     * @param users new network users
     */
    @Override
    public void addAllUsers(Collection<KADAddress> users) {

    }

    /**
     * @return a list of users, there can be {@value #MAX_USER_BUCKET_LENGTH} for each bucket
     */
    @Override
    public ArrayList<KADAddress> getAllUsers() {
        return null;
    }

    /**
     * Removes a user from the list of peers
     * @param user registered user
     */
    @Override
    public void removeUser(KADAddress user) {

    }

    /**
     * Removes a collection of users from the list of peers
     * @param users registered users
     */
    @Override
    public void removeAllUsers(Collection<KADAddress> users) {

    }

    /**
     * @param key   a resource key present in the dictionary
     * @param value updated value of the resource
     * @return the old value of the resource if there was one, null otherwise
     */
    @Override
    public RV setResource(RK key, RV value) {
        return null;
    }

    /**
     * @param resources a map of key and values
     * @return the old values of the resources if there were one, contains null otherwise
     */
    @Override
    public Map<RK, RV> setAllResources(Map<RK, RV> resources) {
        return null;
    }

    /**
     * @param resourceKey identification for the resource
     * @return the value of the removed resource
     */
    @Override
    public RV removeResource(RK resourceKey) {
        return null;
    }

    /**
     * @param resourcesKeys identification keys for the resources
     * @return the values of the removed resources
     */
    @Override
    public ArrayList<RV> removeAllResources(Collection<RK> resourcesKeys) {
        return null;
    }

    /**
     * @param resourceKey identification for the resource
     * @return the value of the giver resource key
     */
    @Override
    public RV getValue(RK resourceKey) {
        return null;
    }

    /**
     * @param resourceKeys identifications for the resources
     * @return a {@link ArrayList} of all values associated to the given keys
     */
    @Override
    public ArrayList<RV> getAllValues(Collection<RK> resourceKeys) {
        return null;
    }

    /**
     * @return All the resources keys associated with this peer
     */
    @Override
    public ArrayList<RK> getKeys() {
        return null;
    }

    /**
     * @return All the resources values associated with this peer
     */
    @Override
    public ArrayList<RV> getValues() {
        return null;
    }
}
