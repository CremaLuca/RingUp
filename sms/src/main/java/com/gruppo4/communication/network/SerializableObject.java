package com.gruppo4.communication.network;

import androidx.annotation.NonNull;

/**
 * Identifies a resource in the network, this is NOT the actual resource.
 * It can contain details for the resource
 */
public abstract class SerializableObject{

    /**
     * Mandatory override for equals()
     *
     * @param other another object
     * @return true if the object represented is the same
     */
    public abstract boolean equals(Object other);

    /**
     * Mandatory override for toString()
     *
     * @return a string representing the state of the object
     */

    @NonNull
    public abstract String toString();

}
