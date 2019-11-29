package com.gruppo4.SMSApp;

/**
 * Manages the state of MainActivity
 */
public class MainActivityHelper {

    public static final String START_ACTIVITY_RING = "start_ring";

    public enum MainActivityState {
        ONPAUSE,
        ONRESUME,
        ONSTOP,
        ONSTART,
        ONCREATE,
        ONDESTROY
    }

    private static MainActivityState state;

    public static  MainActivityState getState() {
        return state;
    }

    public static void setState(MainActivityState state) {
        state = state;
    }
}
