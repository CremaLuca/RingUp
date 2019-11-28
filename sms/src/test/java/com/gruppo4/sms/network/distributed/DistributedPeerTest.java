package com.gruppo4.sms.network.distributed;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

public class DistributedPeerTest {

    private static String DEFAULT_TELEPHONE_NUMBER = "+393533432222";
    private static String DEFAULT_TELEPHONE_NUMBER_SHA_256_HASH = "3d216bd0d4eecf24d8db";
    private DistributedPeer defaultPeer;

    @Before
    public void init() {
        defaultPeer = new DistributedPeer(DEFAULT_TELEPHONE_NUMBER);
    }

    @Test
    public void getHashCode_isRightLength() {
        Assert.assertEquals(DistributedPeer.MAX_ADDRESS_BYTE_LENGTH, defaultPeer.getHashCode().length);
    }

    @Test
    public void getHashCode_content_isEquals() {
        byte[] hash = defaultPeer.getHashCode();
        Assert.assertEquals(DEFAULT_TELEPHONE_NUMBER_SHA_256_HASH, new BigInteger(1, hash).toString(16));
    }
}