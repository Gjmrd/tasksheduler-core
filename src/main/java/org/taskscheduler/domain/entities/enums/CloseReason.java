package org.taskscheduler.domain.entities.enums;


import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CloseReason {

    //todo implement reason enum
    NONE("none"),
    COMPLETED("completed"),
    CANCELED("canceled"),
    DUPLICATE("duplicate");

    private String closeReason;

    private static Map<String, CloseReason> closeReasons = new HashMap<>();

    static {
        for (CloseReason r : EnumSet.allOf(CloseReason.class)) {
            closeReasons.put(r.toString(), r);
        }
    }

    CloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    public CloseReason closeReason(String closeReason) {
        return closeReasons.get(closeReason);
    }

    @Override
    public String toString() {
        return closeReason;
    }


}
