package com.gruppo4.sms.network;

import com.gruppo4.sms.dataLink.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SMSNetworkDictionaryTest {

    private static final String DEFAULT_PEER_ADDRESS = "+393434444543";
    private static final String DEFAULT_RESOURCE_ID = "DefaultResourceID";
    SMSNetworkDictionary dictionary;

    /**
     * @throws Exception ??? Auto-gerated from Android Studio
     */
    @Before
    public void setUp() throws Exception {
        dictionary = new SMSNetworkDictionary();
        SMSPeer user = new SMSPeer(DEFAULT_PEER_ADDRESS);
        TestResource resource = new TestResource(DEFAULT_RESOURCE_ID);
        dictionary.addUser(user);
        dictionary.addResource(user, resource);
    }

    @Test
    public void addUser_user_isEquals() {
        SMSPeer user = new SMSPeer(DEFAULT_PEER_ADDRESS);
        dictionary.addUser(user);
        Assert.assertEquals(user, dictionary.getAllUsers()[0]);
    }

    @Test
    public void addResource_resource_isEquals() {
        SMSPeer user = new SMSPeer(DEFAULT_PEER_ADDRESS);
        TestResource resource = new TestResource(DEFAULT_RESOURCE_ID) {
        };//It's an abstract class, this is an implementation
        dictionary.addResource(user, resource);
        Assert.assertEquals(resource, dictionary.getResourcesByUser(user)[0]);
    }

    @Test
    public void removeUser() {
    }

    @Test
    public void removeResource() {
    }

    @Test
    public void getUsersByResource() {
    }

    @Test
    public void getResourcesByUser() {
    }

    @Test
    public void getAllUsers() {
    }

    @Test
    public void getAllResources() {
    }
}