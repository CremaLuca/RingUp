package com.gruppo4.RingApplication.ringCommands.Interfaces;

public interface PermissionInterface {

    /**
     * Simple method used to check permissions
     */
    void checkPermission();

    /**
     * This method causes the permissions to be requested after a certain amount of time.
     *
     * @param time to wait before checking permits
     */
    void waitForPermissions(int time);

}
