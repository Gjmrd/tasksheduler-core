package org.taskscheduler.rest.dto;

public class GroupDto {
    private String[] members;
    private String name;

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
