package com.gruppo4.sms.network;

import com.gruppo4.communication.network.Resource;
import com.gruppo4.sms.dataLink.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class SMSNetworkDictionaryTest {

    private static final String DEFAULT_PEER_ADDRESS = "+393434444543";
    private static final String DEFAULT_RESOURCE_ID = "DefaultResourceID";

    private static final SMSPeer DEFAULT_USER_1 = new SMSPeer(DEFAULT_PEER_ADDRESS);
    private static final SMSPeer DEFAULT_USER_2 = new SMSPeer(DEFAULT_PEER_ADDRESS + "1");
    private static final TestResource DEFAULT_RESOURCE_1 = new TestResource(DEFAULT_RESOURCE_ID);
    private static final TestResource DEFAULT_RESOURCE_2 = new TestResource(DEFAULT_RESOURCE_ID + "1");
    private static final TestResource[] DEFAULT_RESOURCE_ARRAY = new TestResource[]{DEFAULT_RESOURCE_1, DEFAULT_RESOURCE_2};
    private static final SMSPeer[] DEFAULT_USER_ARRAY = new SMSPeer[]{DEFAULT_USER_1, DEFAULT_USER_2};

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

    private boolean compareArray(Object[] array1, Object[] array2) {
        HashSet<Object> set1 = new HashSet<>(Arrays.asList(array1));
        HashSet<Object> set2 = new HashSet<>(Arrays.asList(array2));
        return set1.equals(set2);
    }

    @Test
    public void getAllUsers_user_isEquals() {
        Assert.assertTrue(compareArray(DEFAULT_USER_ARRAY, dictionary.getAllUsers()));
    }

    @Test
    public void getAllResources_resources_areEquals() {
        Resource[] allResources = dictionary.getAllResources();
        Assert.assertTrue(compareArray(DEFAULT_RESOURCE_ARRAY, allResources));
    }

    @Test
    public void removeUser_users_areEquals() {
        dictionary.removeUser(DEFAULT_USER_1);
        Assert.assertTrue(compareArray(new SMSPeer[]{DEFAULT_USER_2}, dictionary.getAllUsers()));
    }

    @Test
    public void removeUser_resources_areEquals() {
        dictionary.removeUser(DEFAULT_USER_1);
        Assert.assertTrue(compareArray(new TestResource[]{DEFAULT_RESOURCE_1}, dictionary.getAllResources()));
    }

    @Test
    public void removeResource_resources_areEquals() {
        dictionary.removeResource(DEFAULT_USER_1, DEFAULT_RESOURCE_1);
        Assert.assertTrue(compareArray(DEFAULT_RESOURCE_ARRAY, dictionary.getAllResources()));
    }

    @Test
    public void getUsersByResource() {
        Resource[] arr1 = new TestResource[]{DEFAULT_RESOURCE_1, DEFAULT_RESOURCE_2};
        TestResource[] arr2 = new TestResource[]{DEFAULT_RESOURCE_2, DEFAULT_RESOURCE_1};
        Assert.assertTrue(compareArray(arr1, arr2));
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