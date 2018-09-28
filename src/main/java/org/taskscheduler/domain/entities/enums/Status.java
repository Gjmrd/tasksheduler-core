package org.taskscheduler.domain.entities.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Status {

    ONGOING("ongoing"),
    FREEZED("freezed"),
    FINISHED("finished"),
    CLOSED("closed");

    private String status;

    private static Map<String, Status> statuses = new HashMap<>();

    Status(String status) {
        this.status = status;
    }

    static {
        for (Status t : EnumSet.allOf(Status.class)) {
            statuses.put(t.toString(), t);
        }
    }

    public Status getStatus(String status) {
        return statuses.get(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
