package com.gruppo4.sms.network;

import com.gruppo4.communication.network.Resource;

/**
 * Test resource used in unit tests
 */
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
    public boolean isEquals(Resource<String> other) {
        if (!(other instanceof TestResource))
            return false;
        TestResource otherResource = (TestResource) other;
        return otherResource.resourceName.equals(resourceName);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TestResource))
            return false;
        return isEquals((TestResource) other);
    }


}
