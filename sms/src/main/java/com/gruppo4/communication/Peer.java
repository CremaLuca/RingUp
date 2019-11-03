package com.gruppo4.communication;

import androidx.annotation.NonNull;

public abstract class Peer<A> {

    private A address;

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
