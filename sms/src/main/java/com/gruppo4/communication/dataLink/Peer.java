package com.gruppo4.communication.dataLink;

import androidx.annotation.NonNull;

public abstract class Peer<A> {

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
    public abstract String toString();

}
