package com.gruppo4.sms.network.distributed;

import com.gruppo4.communication.network.NetworkDictionary;
import com.gruppo4.sms.dataLink.SMSPeer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class SMSDistributedNetworkDictionary<RK, RV> implements NetworkDictionary<SMSPeer, RK, RV> {
    @Override
    public void addUser(SMSPeer user) {

    }

    @Override
    public void addAllUsers(Collection<SMSPeer> users) {

    }

    @Override
    public ArrayList<SMSPeer> getAllUsers() {
        return null;
    }

    @Override
    public void removeUser(SMSPeer user) {

    }

    @Override
    public void removeAllUsers(Collection<SMSPeer> users) {

    }

    @Override
    public RV setResource(RK key, RV value) {
        return null;
    }

    @Override
    public Map<RK, RV> setAllResources(Map<RK, RV> resources) {
        return null;
    }

    @Override
    public RV removeResource(RK resourceKey) {
        return null;
    }

    @Override
    public ArrayList<RV> removeAllResources(Collection<RK> resourcesKeys) {
        return null;
    }

    @Override
    public RV getValue(RK resourceKey) {
        return null;
    }

    @Override
    public ArrayList<RV> getAllValues(Collection<RK> resourceKeys) {
        return null;
    }

    @Override
    public ArrayList<RK> getKeys() {
        return null;
    }

    @Override
    public ArrayList<RV> getValues() {
        return null;
    }
}
