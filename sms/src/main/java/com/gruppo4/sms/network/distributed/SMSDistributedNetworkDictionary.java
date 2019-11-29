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
    private KADAddress userAddress;
    private ArrayList<KADAddress>[] usersLists;

    public SMSDistributedNetworkDictionary(KADAddress userAddress) {
        this.userAddress = userAddress;
        usersLists = new ArrayList[KADAddress.BYTE_ADDRESS_LENGTH * Byte.SIZE];
    }

    /**
     * Adds a user at the end of the list of users to contact.
     *
     * @param newUser new network user. Must not be the current user.
     */
    @Override
    public void addUser(KADAddress newUser) {
        //Calculate the distance (to understand what bucket you have to place him)
        int bucketIndex = userAddress.firstDifferentBitPosition(newUser);

        //If it's actually the current user we don't add itself
        if(bucketIndex >= (KADAddress.BYTE_ADDRESS_LENGTH * Byte.SIZE))
            return;

        if (usersLists[bucketIndex] == null)
            usersLists[bucketIndex] = new ArrayList<>();
        else if (usersLists[bucketIndex].contains(newUser))
            return;

        usersLists[bucketIndex].add(newUser);
    }

    /**
     * Adds the collection of users to the end of the user list
     *
     * @param users new network users
     */
    @Override
    public void addAllUsers(Collection<KADAddress> users) {
        for (KADAddress user : users) {
            addUser(user);
        }
    }

    /**
     * Returns an array of all users indexed.
     * Use with caution, they are not divided by bucket.
     *
     * @return a list of all users.
     */
    @Override
    public ArrayList<KADAddress> getAllUsers() {
        ArrayList<KADAddress> returnList = new ArrayList<>();

        for(int i=0;i<usersLists.length;i++){
            if(usersLists[i] != null)
                returnList.addAll(usersLists[i]);
        }
        return returnList;
    }

    /**
     * @param distance what position is the first different bit, counting from left
     * @return an ArrayList of users in that particular bucket, empty ArrayList if there is none
     */
    public ArrayList<KADAddress> getUsersByDistance(int distance){
        if(usersLists[distance] != null)
            return new ArrayList<>(usersLists[distance]);
        return new ArrayList<>();
    }

    /**
     * Removes a user from the list of peers
     *
     * @param user registered user
     */
    @Override
    public void removeUser(KADAddress user) {
        int bucketIndex = userAddress.firstDifferentBitPosition(user);
        if(bucketIndex >= KADAddress.BYTE_ADDRESS_LENGTH * Byte.SIZE)
            throw new  IllegalArgumentException("Cannot remove itself");
        if(usersLists[bucketIndex] == null)
            throw new IllegalArgumentException("User is not actually present in the list");
        if(!usersLists[bucketIndex].remove(user))
            throw new IllegalArgumentException("User is not actually present in the user list");
    }

    /**
     * Removes a collection of users from the list of peers
     *
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
