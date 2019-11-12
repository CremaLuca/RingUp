package com.gruppo4.communication.network;

import java.io.Serializable;

/**
 * Identifies a resource in the network, this is NOT the actual resource.
 * It can contain details for the resource
 */
public abstract class Resource{

    /**
     * Mandatory override for equals()
     *
     * @param other another object
     * @return true if the resource represented is the same
     */
    public abstract  boolean equals(Object other);
    public abstract int hashCode();
    public abstract String toString(); //serialization
}
