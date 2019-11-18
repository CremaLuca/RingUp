
package com.gruppo4.sms.network;


/**
 * Test resource used in unit tests
 */

class TestResource {

    private String resourceName;

    TestResource(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TestResource))
            return false;
        TestResource otherResource = (TestResource) other;
        return resourceName.equals(otherResource.resourceName);
    }

    @Override
    public int hashCode() {
        return resourceName.hashCode();
    }

    @Override
    public String toString() {
        return resourceName;
    }


}