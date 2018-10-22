package org.taskscheduler.services;

import org.springframework.data.domain.Pageable;
import org.taskscheduler.domain.entities.PageInfo;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.TaskLog;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.domain.entities.enums.Status;

import java.util.Date;

public interface TaskLogService {
    TaskLog getById(long id);
    TaskLog save(TaskLog tasklog);
    TaskLog create(Task task);
    PageInfo<TaskLog> getLogsByTask(Task task, Pageable pageable);
    PageInfo<TaskLog> getLogsByTask(Task task, Date startDate, Date endDate, Pageable pageable);
}
