package com.yxy.nova.mwh.kafka.consumer;

/**
 * Created by xiazhen on 18/6/26.
 */
public enum Mode {

    STANDARD,
    PARANOID //do not use paranoid mode unless you are pretty sure about what is going on here !!!
    ;

    public static Mode of (final String mode) {
        for (final Mode m : Mode.values()) {
            if (m.name().equalsIgnoreCase(mode)) {
                return m;
            }
        }
        return STANDARD;
    }
}
