package com.gruppo4.communication;

import androidx.annotation.NonNull;

public class Peer {

    private String address;

    public Peer(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    protected void setAddress(String address) {
        this.address = address;
    }

    @NonNull
    @Override
    public String toString() {
        return address;
    }

}
