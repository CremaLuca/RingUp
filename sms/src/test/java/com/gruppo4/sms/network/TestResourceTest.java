package com.gruppo4.sms.network;

import org.junit.Assert;
import org.junit.Test;

public class TestResourceTest {

    @Test
    public void getID() {
    }

    @Test
    public void isEquals_isTrue() {
        TestResource res1 = new TestResource("CIAO");
        TestResource res2 = new TestResource("CIAO");
        Assert.assertTrue(res1.isEquals(res2));
    }

    @Test
    public void isEquals_isFalse() {
        TestResource res1 = new TestResource("CIAO");
        TestResource res2 = new TestResource("CIAO1");
        Assert.assertFalse(res1.isEquals(res2));
    }

    @Test
    public void equals_isTrue() {
        TestResource res1 = new TestResource("CIAO");
        TestResource res2 = new TestResource("CIAO");
        Assert.assertTrue(res1.equals(res2));
    }
}