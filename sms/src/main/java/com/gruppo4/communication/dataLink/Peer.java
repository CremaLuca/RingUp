package com.gruppo4.communication.dataLink;

import androidx.annotation.NonNull;

import java.io.Serializable;

public abstract class Peer<A> implements Serializable {

    protected A address;

    public Peer(A address) {
        this.address = address;
    }

    public A getAddress() {
        return address;
    }

    protected void setAddress(A address) {
        this.address = address;
    }

    @NonNull
    @Override
    public String toString() {
        return address.toString();
    }

}
