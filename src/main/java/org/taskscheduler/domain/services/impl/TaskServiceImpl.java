package org.taskscheduler.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.domain.entities.enums.Status;
import org.taskscheduler.domain.interfaces.repositories.TaskRepository;
import org.taskscheduler.domain.services.TaskService;

import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;


    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task getById(int id) throws Exception {
        return taskRepository.findById(id).orElseGet(null);
    }

    @Override
    public List<Task> getCreated(User user) throws Exception {
        return taskRepository.findCreated(user);
    }

    @Override
    public List<Task> getCreatedBetween(Date startDate, Date endDate, User user) {
        return taskRepository.findCreatedBetween(startDate, endDate, user);
    }

    @Override
    public List<Task> getAll() throws Exception {
        return taskRepository.findAll();
    }

    @Override
    public void save(Task task) {
        taskRepository.save(task);
    }

    @Override
    public void delete(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public void close(Task task, CloseReason reason) {
            task.setStatus(Status.CLOSED);
            task.setCloseReason(reason);
            task.setClosedAt(new Date());
            taskRepository.save(task);
    }
}
