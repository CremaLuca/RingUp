package com.gruppo4.sms.network;

import com.gruppo4.sms.dataLink.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SMSNetworkDictionaryTest {

    private static final String DEFAULT_PEER_ADDRESS = "+393434444543";
    private static final String DEFAULT_RESOURCE_ID = "DefaultResourceID";

    private static final SMSPeer defaultUser1 = new SMSPeer(DEFAULT_PEER_ADDRESS);
    private static final SMSPeer defaultUser2 = new SMSPeer(DEFAULT_PEER_ADDRESS + "1");
    private static final TestResource defaultResource1 = new TestResource(DEFAULT_RESOURCE_ID);
    private static final TestResource resource2 = new TestResource(DEFAULT_RESOURCE_ID + "1");

    private SMSNetworkDictionary dictionary;

    /**
     * @throws Exception ??? Auto-generated from Android Studio
     */
    @Before
    public void setUp() throws Exception {
        dictionary = new SMSNetworkDictionary();
        dictionary.addUser(defaultUser1);
        dictionary.addResource(defaultUser1, defaultResource1);
    }

    @Test
    public void getAllUsers_user_isEquals() {
        Assert.assertEquals(defaultUser1, dictionary.getAllUsers()[0]);
    }

    @Test
    public void getAllResources_resource_isEquals() {
        Assert.assertEquals(defaultResource1, dictionary.getAllResources()[0]);
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