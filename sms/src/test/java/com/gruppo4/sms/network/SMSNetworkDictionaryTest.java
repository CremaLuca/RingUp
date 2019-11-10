package com.gruppo4.sms.network;

import com.gruppo4.communication.network.Resource;
import com.gruppo4.sms.dataLink.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SMSNetworkDictionaryTest {

    private static final String DEFAULT_PEER_ADDRESS = "+393434444543";
    private static final String DEFAULT_RESOURCE_ID = "DefaultResourceID";

    private static final SMSPeer DEFAULT_USER_1 = new SMSPeer(DEFAULT_PEER_ADDRESS);
    private static final SMSPeer DEFAULT_USER_2 = new SMSPeer(DEFAULT_PEER_ADDRESS + "1");
    private static final TestResource DEFAULT_RESOURCE_1 = new TestResource(DEFAULT_RESOURCE_ID);
    private static final TestResource DEFAULT_RESOURCE_2 = new TestResource(DEFAULT_RESOURCE_ID + "1");
    private static final TestResource[] DEFAULT_RESOURCE_ARRAY = new TestResource[]{DEFAULT_RESOURCE_1, DEFAULT_RESOURCE_2};

    private SMSNetworkDictionary dictionary;

    /**
     * @throws Exception ??? Auto-generated from Android Studio
     */
    @Before
    public void setUp() throws Exception {
        dictionary = new SMSNetworkDictionary();
        dictionary.addUser(DEFAULT_USER_1);
        dictionary.addUser(DEFAULT_USER_2);
        dictionary.addResource(DEFAULT_USER_1, DEFAULT_RESOURCE_1);
        dictionary.addResource(DEFAULT_USER_1, DEFAULT_RESOURCE_2);
        dictionary.addResource(DEFAULT_USER_2, DEFAULT_RESOURCE_1);
    }

    @Test
    public void getAllUsers_user_isEquals() {
        Assert.assertEquals(DEFAULT_USER_1, dictionary.getAllUsers()[0]);
    }

    @Test
    public void getAllResources_resources_areEquals() {
        Resource[] allResources = dictionary.getAllResources();
        Assert.assertEquals(DEFAULT_RESOURCE_ARRAY, allResources);
    }

    @Test
    public void removeUser_users_areEquals() {
        dictionary.removeUser(DEFAULT_USER_1);
        Assert.assertEquals(new SMSPeer[]{DEFAULT_USER_2}, dictionary.getAllUsers());
    }

    @Test
    public void removeUser_resources_areEquals() {
        dictionary.removeUser(DEFAULT_USER_1);
        Assert.assertEquals(new TestResource[]{DEFAULT_RESOURCE_1}, dictionary.getAllResources());
    }

    @Test
    public void removeResource_resources_areEquals() {
        dictionary.removeResource(DEFAULT_USER_1, DEFAULT_RESOURCE_1);
        Assert.assertEquals(DEFAULT_RESOURCE_ARRAY, dictionary.getAllResources());
    }

    @Test
    public void getUsersByResource() {
        TestResource[] arr1 = new TestResource[]{DEFAULT_RESOURCE_1};
        TestResource[] arr2 = new TestResource[]{DEFAULT_RESOURCE_1};
        Assert.assertEquals(arr1, arr2);
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