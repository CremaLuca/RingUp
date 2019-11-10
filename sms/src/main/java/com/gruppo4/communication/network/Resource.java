package com.gruppo4.communication.network;

/**
 * Identifies a resource in the network, this is NOT the actual resource.
 * It can contain details for the resource, useful to be
 *
 * @param <T> The identifier
 */
public interface Resource<T> {

    /**
     * Identifies the resource uniquely in the network
     * Can be a hashcode or the name
     *
     * @return the identifier
     */
    T getID();

    /**
     * Mandatory override for equals()
     *
     * @param other another object
     * @return true if the resource represented is the same
     */
    boolean isEquals(Resource<T> other);

}
