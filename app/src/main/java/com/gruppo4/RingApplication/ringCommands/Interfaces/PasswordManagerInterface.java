package com.gruppo4.RingApplication.ringCommands.Interfaces;

public interface PasswordManagerInterface {

    /**
     * @return the password saved in memory
     */
    String getPassword();

    /**
     * @param password password that want to be saved in memory
     */
    void setPassword(String password);

    /**
     * Checks if there's a password saved in memory
     *
     * @return true if there's a password saved in memory, false otherwise
     */
    boolean isPassSaved();

    /**
     * Deletes the saved password
     */
    void deletePassword();

}
