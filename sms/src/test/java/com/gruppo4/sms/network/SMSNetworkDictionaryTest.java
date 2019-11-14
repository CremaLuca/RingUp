package com.gruppo4.sms.network;

import com.gruppo4.communication.network.Resource;
import com.gruppo4.sms.dataLink.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class SMSNetworkDictionaryTest {

    private static final String DEFAULT_PEER_ADDRESS = "+393434444543";
    private static final String DEFAULT_RESOURCE_ID = "DefaultResourceID";

    private static final SMSPeer DEFAULT_USER_1 = new SMSPeer(DEFAULT_PEER_ADDRESS);
    private static final SMSPeer DEFAULT_USER_2 = new SMSPeer(DEFAULT_PEER_ADDRESS + "1");
    private static final SMSPeer DEFAULT_USER_3 = new SMSPeer(DEFAULT_PEER_ADDRESS + "2");
    private static final SMSPeer DEFAULT_USER_4 = new SMSPeer("+393434444544");
    private static final SMSPeer COPY_USER_1 = new SMSPeer(DEFAULT_PEER_ADDRESS);
    private static final TestResource DEFAULT_RESOURCE_1 = new TestResource(DEFAULT_RESOURCE_ID);
    private static final TestResource DEFAULT_RESOURCE_2 = new TestResource(DEFAULT_RESOURCE_ID + "1");
    private static final TestResource DEFAULT_RESOURCE_3 = new TestResource(DEFAULT_RESOURCE_ID + "2");
    private static final TestResource[] DEFAULT_RESOURCE_ARRAY = new TestResource[]{DEFAULT_RESOURCE_1, DEFAULT_RESOURCE_2};
    private static final SMSPeer[] DEFAULT_USER_ARRAY = new SMSPeer[]{DEFAULT_USER_1, DEFAULT_USER_2};

    private SMSNetworkDictionary<SMSPeer, TestResource> defaultDictionary;

    /**
     * @throws Exception ??? Auto-generated from Android Studio
     */
    @Before
    public void setUp() throws Exception {
        defaultDictionary = new SMSNetworkDictionary<>();
        defaultDictionary.addUser(DEFAULT_USER_2);
        defaultDictionary.addUser(DEFAULT_USER_1);
        defaultDictionary.setResource(DEFAULT_USER_1, DEFAULT_RESOURCE_1);
        defaultDictionary.setResource(DEFAULT_USER_2, DEFAULT_RESOURCE_2);
    }

    private boolean compareArray(Object[] array1, Object[] array2) {
        HashSet<Object> set1 = new HashSet<>(Arrays.asList(array1));
        HashSet<Object> set2 = new HashSet<>(Arrays.asList(array2));
        return set1.equals(set2);
    }

    private boolean compareArray(Object[] array1, ArrayList array2) {
        HashSet<Object> set1 = new HashSet<>(Arrays.asList(array1));
        HashSet set2 = new HashSet<>(array2);
        return set1.equals(set2);
    }

    private boolean compareArray(ArrayList array1, ArrayList array2) {
        HashSet set1 = new HashSet<>(array1);
        HashSet set2 = new HashSet<>(array2);
        return set1.equals(set2);
    }

    @Test
    public void getAllUsers_user_isEquals() {
        Assert.assertTrue(compareArray(DEFAULT_USER_ARRAY, defaultDictionary.getAllUsers()));
    }

    @Test
    public void getAllResources_resources_areEquals() {
        ArrayList<TestResource> allResources = defaultDictionary.getAllValues();
        Assert.assertTrue(compareArray(DEFAULT_RESOURCE_ARRAY, allResources));
    }

    @Test
    public void removeUser_users_areEquals() {
        defaultDictionary.removeUser(DEFAULT_USER_1);
        Assert.assertTrue(compareArray(new SMSPeer[]{DEFAULT_USER_2}, defaultDictionary.getAllUsers()));
    }

    @Test
    public void removeUser_users_areEquals2() {
        defaultDictionary.removeUser(DEFAULT_USER_2);
        Assert.assertTrue(compareArray(new SMSPeer[]{DEFAULT_USER_1}, defaultDictionary.getAllUsers()));
    }

    @Test
    public void removeUser_nonExistentUser_users_areEquals() {
        defaultDictionary.removeUser(DEFAULT_USER_3);
        Assert.assertTrue(compareArray(DEFAULT_USER_ARRAY, defaultDictionary.getAllUsers()));
    }

    @Test
    public void removeUser_users_isEmpty() {
        defaultDictionary.removeUser(DEFAULT_USER_1);
        defaultDictionary.removeUser(DEFAULT_USER_2);
        Assert.assertTrue(compareArray(new SMSPeer[0], defaultDictionary.getAllUsers()));
    }

    @Test
    public void getValue_value_isEquals() {
        Assert.assertEquals(DEFAULT_RESOURCE_1, defaultDictionary.getValue(DEFAULT_USER_1));
    }

    @Test
    public void getValue_value_isEquals2() {
        Assert.assertEquals(DEFAULT_RESOURCE_2, defaultDictionary.getValue(DEFAULT_USER_2));
    }

    @Test
    public void getValue_nonExistentKey_resource_isEmpty() {
        Assert.assertNull(defaultDictionary.getValue(DEFAULT_USER_3));
    }

    @Test
    public void addUser_duplicate_isNotInserted() {
        defaultDictionary.addUser(DEFAULT_USER_1);
        Assert.assertTrue(compareArray(DEFAULT_USER_ARRAY, defaultDictionary.getAllUsers()));
    }

    @Test
    public void addUser_almostDuplicate_isNotInserted() {
        defaultDictionary.addUser(COPY_USER_1);
        Assert.assertTrue(compareArray(DEFAULT_USER_ARRAY, defaultDictionary.getAllUsers()));
    }

    @Test
    public void getAllUsers_users_areOrdered() {
        defaultDictionary.addUser(DEFAULT_USER_4);
        SMSPeer[] testOrderedArray = new SMSPeer[]{DEFAULT_USER_1, DEFAULT_USER_2, DEFAULT_USER_4};
        Assert.assertArrayEquals(testOrderedArray, defaultDictionary.getAllUsers().toArray());
    }

    @Test
    public void removeResource_keys_areEquals() {
        Assert.assertNotNull(defaultDictionary.removeResource(DEFAULT_USER_2));
        Assert.assertTrue(compareArray(new SMSPeer[]{DEFAULT_USER_1}, defaultDictionary.getAllKeys()));
    }

    @Test
    public void removeResource_values_areEquals() {
        Assert.assertNotNull(defaultDictionary.removeResource(DEFAULT_USER_2));
        Assert.assertTrue(compareArray(new TestResource[]{DEFAULT_RESOURCE_1}, defaultDictionary.getAllValues()));
    }

    @Test
    public void getAllKeys_keys_areEquals() {
        Assert.assertTrue(compareArray(DEFAULT_USER_ARRAY, defaultDictionary.getAllKeys()));
    }

    @Test
    public void test_compareArray_isCorrect() {
        Resource[] arr1 = new TestResource[]{DEFAULT_RESOURCE_1, DEFAULT_RESOURCE_2};
        TestResource[] arr2 = new TestResource[]{DEFAULT_RESOURCE_2, DEFAULT_RESOURCE_1};
        Assert.assertTrue(compareArray(arr1, arr2));
    }


}