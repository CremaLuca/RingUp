package com.gruppo4.sms.network.distributed;

import org.junit.Before;
import org.junit.Test;

public class SMSDistributedNetworkDictionaryTest {

    private static String DEFAULT_TELEPHONE_NUMBER = "+393343343332";
    private static DistributedPeer DEFAULT_VALID_PEER = new DistributedPeer(DEFAULT_TELEPHONE_NUMBER);
    private SMSDistributedNetworkDictionary defaultDictionary;

    @Before
    public void init() {
        defaultDictionary = new SMSDistributedNetworkDictionary();
        defaultDictionary.addUser(DEFAULT_VALID_PEER);
    }

    @Test
    public void addAllUsers() {
    }

    @Test
    public void getAllUsers() {
    }

    @Test
    public void removeUser() {
    }

    @Test
    public void removeAllUsers() {
    }

    @Test
    public void setResource() {
    }

    @Test
    public void setAllResources() {
    }

    @Test
    public void removeResource() {
    }

    @Test
    public void removeAllResources() {
    }

    @Test
    public void getValue() {
    }

    @Test
    public void getAllValues() {
    }

    @Test
    public void getKeys() {
    }

    @Test
    public void getValues() {
    }
}