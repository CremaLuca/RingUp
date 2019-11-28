package com.gruppo4.sms.network.distributed;

import com.gruppo4.communication.dataLink.Peer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KADAddress extends Peer<byte[]> {

    public static final String HASH_ALGORITHM = "SHA-256";
    public static final int BYTE_ADDRESS_LENGTH = 10;

    private byte[] address;

    public KADAddress(byte[] address) throws IllegalArgumentException {
        super();
        if (address.length != BYTE_ADDRESS_LENGTH)
            throw new IllegalArgumentException("Byte address should be " + BYTE_ADDRESS_LENGTH + " bytes long");
        this.address = address;
    }

    public KADAddress(Peer peer) {
        try {
            MessageDigest digestAlgorithm = MessageDigest.getInstance(HASH_ALGORITHM);
            address = new byte[BYTE_ADDRESS_LENGTH];
            System.arraycopy(digestAlgorithm.digest(peer.getAddress().getBytes(StandardCharsets.UTF_8)), 0, address, 0, BYTE_ADDRESS_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return null;
    }

}
