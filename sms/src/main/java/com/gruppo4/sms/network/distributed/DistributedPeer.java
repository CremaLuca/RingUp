package com.gruppo4.sms.network.distributed;

import com.gruppo4.sms.dataLink.SMSPeer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DistributedPeer extends SMSPeer {

    public static int MAX_ADDRESS_BYTE_LENGTH = SMSDistributedNetworkDictionary.MAX_BYTE_ADDRESS_LENGTH;

    /**
     * @param telephoneNumber a valid telephone number (checkTelephoneNumber state must be TELEPHONE_NUMBER_VALID)
     */
    public DistributedPeer(String telephoneNumber) {
        super(telephoneNumber);
    }

    /**
     * @return an array of {@value #MAX_ADDRESS_BYTE_LENGTH} bytes long
     */
    public byte[] getHashCode() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] returnBytes = new byte[MAX_ADDRESS_BYTE_LENGTH];
            System.arraycopy(digest.digest(getAddress().getBytes(StandardCharsets.UTF_8)), 0, returnBytes, 0, MAX_ADDRESS_BYTE_LENGTH);
            return returnBytes;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
