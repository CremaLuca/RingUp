package com.gruppo4.sms.network;

import com.gruppo4.communication.network.Resource;

public abstract class SMSResource implements Resource<String> {

    private String resourceName;
    //EXTRA : there could be something like GeolocalPosition position

    public SMSResource(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }

}
