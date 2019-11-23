package com.gruppo4.RingApplication.ringCommands.Interfaces;

public interface PasswordManagerInterface {

    /**
     * @return the password saved in memory
     */
    String getPassword();

    /**
     * @param password that want to be saved in memory
     */
    void setPassword(String password);

    /**
     * Deletes the password saved in memory
     */
    void deletePassword();

    /**
     * Checks if there's a password saved in memory
     *
     * @return true if is it present, false otherwise
     */
    boolean isPassSaved();
}
