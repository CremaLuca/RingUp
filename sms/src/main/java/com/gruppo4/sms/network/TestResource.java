/*
package com.gruppo4.sms.network;

import com.gruppo4.communication.network.Resource;

*/
/**
 * Test resource used in unit tests
 *//*

class TestResource implements Resource<String> {

    private String resourceName;

    TestResource(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String getID() {
        return resourceName;
    }

    @Override
    public boolean isEquals(Resource other) {
        if (!(other instanceof TestResource))
            return false;
        TestResource otherResource = (TestResource) other;
        return getID().equals(otherResource.getID());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TestResource))
            return false;
        return isEquals((TestResource) other);
    }


}
*/
