package org.taskscheduler.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class TaskDto implements Serializable{
    private String caption;
    private String description;
    private String responsibleName;
    private String[] executors;
    private Date deadlineAt;

    @NotNull
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }

    @NotNull
    public String[] getExecutors() {
        return executors;
    }

    public void setExecutors(String[] executors) {
        this.executors = executors;
    }

    public Date getDeadlineAt() {
        return deadlineAt;
    }

    public void setDeadlineAt(Date deadlineAt) {
        this.deadlineAt = deadlineAt;
    }
}
