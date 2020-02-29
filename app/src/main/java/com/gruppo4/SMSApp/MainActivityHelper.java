package com.gruppo4.SMSApp;

/**
 * Sets, updates and returns the state of MainActivity
 *
 * Written for future uses
 *
 * @author Marco Tommasini
 */
public class MainActivityHelper {

    public enum MainActivityState {
        ONPAUSE,
        ONRESUME,
        ONSTOP,
        ONSTART,
        ONCREATE,
        ONDESTROY
    };

    private static MainActivityState state;

    public static  MainActivityState getState() {
        return state;
    }

    static void setState(MainActivityState updatedState) {
        state = updatedState;
    }
}
