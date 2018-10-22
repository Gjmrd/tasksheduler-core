package org.taskscheduler.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.taskscheduler.domain.entities.PageInfo;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.TaskLog;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.domain.entities.enums.Status;
import org.taskscheduler.domain.repositories.TaskLogRepository;
import org.taskscheduler.services.TaskLogService;

import java.util.Date;

@Service
public class TaskLogServiceImpl implements TaskLogService {

    private TaskLogRepository taskLogRepository;


    @Autowired
    public TaskLogServiceImpl(TaskLogRepository taskLogRepository) {
        this.taskLogRepository = taskLogRepository;
    }

    @Override
    public TaskLog getById(long id) {
        return taskLogRepository.findById(id).orElse(null);
    }

    @Override
    public TaskLog save(TaskLog tasklog) {
        return taskLogRepository.save(tasklog);
    }



    @Override
    public TaskLog create(Task task) {
        TaskLog taskLog = new TaskLog();
        taskLog.setTask(task);
        taskLog.setStatus(task.getStatus());
        taskLog.setCloseReason(task.getCloseReason());
        taskLogRepository.save(taskLog);
        return taskLog;
    }

    @Override
    public PageInfo<TaskLog> getLogsByTask(Task task, Pageable pageable) {
        return new PageInfo<>(taskLogRepository.getAllByTask(task, pageable));

    }

    @Override
    public PageInfo<TaskLog> getLogsByTask(Task task, Date startDate, Date endDate, Pageable pageable) {
        return new PageInfo<>(taskLogRepository.getAllByTask(task, startDate, endDate, pageable));
    }
}
