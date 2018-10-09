package org.taskscheduler.rest.controllers.dto;

import java.io.Serializable;
import java.util.Date;

public class TaskDto implements Serializable{
    private String caption;
    private String description;
    private String responsibleName;
    private Date deadlineAt;

}
