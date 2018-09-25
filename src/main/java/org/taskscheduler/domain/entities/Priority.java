package org.taskscheduler.domain.entities;


import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Priority {

    HIGHEST ("HUYNYA"),
    LOWEST ("NE HUYNYA");

    private String priority;

    private static Map<String, Priority> priorities = new HashMap<>();

    static {
        for (Priority p : EnumSet.allOf(Priority.class)) {
            priorities.put(p.toString(), p);
        }
    }

    Priority(String priority) {
        this.priority = priority;
    }

    public Priority getPriority(String priority) {
        return priorities.get(priority);
    }

    @Override
    public String toString() {
        return priority;
    }


}
