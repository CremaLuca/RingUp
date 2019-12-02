package com.gruppo4.sms.network.distributed;

import com.eis.communication.Peer;
import com.eis.smslibrary.SMSPeer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class KADPeerAddress implements Peer<byte[]> {

    public static final String HASH_ALGORITHM = "SHA-256";
    public static final int BYTE_ADDRESS_LENGTH = 10;

    private byte[] address;

    public KADPeerAddress(byte[] address) throws IllegalArgumentException {
        //super(null);//Badly designed Peer, will change to AbstractPeer
        if (address.length != BYTE_ADDRESS_LENGTH)
            throw new IllegalArgumentException("Byte address should be " + BYTE_ADDRESS_LENGTH + " bytes long");
        this.address = address;
    }

    public KADPeerAddress(String phoneAddress) {
        //super(null);
        try {
            MessageDigest digestAlgorithm = MessageDigest.getInstance(HASH_ALGORITHM);
            address = new byte[BYTE_ADDRESS_LENGTH];
            System.arraycopy(digestAlgorithm.digest(phoneAddress.getBytes(StandardCharsets.UTF_8)), 0, address, 0, BYTE_ADDRESS_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public KADPeerAddress(SMSPeer peer) {
        this(peer.getAddress());
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public byte[] getAddress() {
        return address;
    }

    /**
     * Calculates the first bit that differs starting from left
     *
     * @param otherAddress
     * @return
     */
    public int firstDifferentBitPosition(KADPeerAddress otherAddress) {
        byte[] otherByteAddress = otherAddress.getAddress();
        BitSet userBitSet = BitSet.valueOf(address);
        BitSet otherBitSet = BitSet.valueOf(otherByteAddress);

        int lastValueIndex = (BYTE_ADDRESS_LENGTH * Byte.SIZE) - 1;

        for (int i = 0; i < otherByteAddress.length; i++) {
            for (int bitCounter = 0; bitCounter < Byte.SIZE; bitCounter++) {
                //You need to reverse the value of the pointer
                int pointer = lastValueIndex - (i + bitCounter);
                //System.out.println("First bit is: " + userBitSet.get(pointer) + " second bit is: " + otherBitSet.get(pointer)); //Testing purposes
                if (userBitSet.get(pointer) != otherBitSet.get(pointer)) {
                    return i + bitCounter;
                }
            }
        }
        return BYTE_ADDRESS_LENGTH * Byte.SIZE;
    }
}
