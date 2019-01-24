package org.taskscheduler.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.taskscheduler.domain.entities.PageInfo;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.TaskLog;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.domain.entities.enums.Priority;
import org.taskscheduler.domain.entities.enums.Status;
import org.taskscheduler.domain.repositories.TaskLogRepository;
import org.taskscheduler.domain.repositories.TaskRepository;
import org.taskscheduler.domain.repositories.UserRepository;
import org.taskscheduler.domain.services.TaskService;
import org.taskscheduler.rest.dto.TaskDto;


import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;



@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskLogRepository taskLogRepository;

    private Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository,
                           TaskLogRepository taskLogRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskLogRepository = taskLogRepository;
    }

    @Override
    public Task getById(long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public PageInfo<Task> getUsersTasks(Date from, Date to, User user, Pageable pageable) {
        return new PageInfo<>(taskRepository.findUsersTasks(from, to, user, pageable));
    }

    @Override
    public PageInfo<User> getExecutorsByTaskId(long taskId, Pageable pageable) {
        return new PageInfo<>(taskRepository.getExecutorsByTaskId(taskId, pageable));
    }

    @Override
    public Task createNew(long userId, TaskDto taskDto) throws ExecutionException, InterruptedException {
        Task task = new Task();
        CompletableFuture future = CompletableFuture
                .supplyAsync(() -> userRepository.findByUsernameArray(taskDto.getExecutors()))
                .thenAccept(task::setExecutors);
        task.setCaption(taskDto.getCaption());
        task.setDescription(taskDto.getDescription());
        task.setPriority(Priority.HIGHEST);
        task.setStatus(Status.ONGOING);
        task.setCreatorId(userId);
        future.get();
        CompletableFuture.runAsync(() -> taskLogRepository.save(new TaskLog(userId, task, "status", Status.NONE.toString(), Status.UNACCEPTED.toString() )));
        taskRepository.save(task);
        logger.info("task %o has been created", task.getId());
        return task;
    }

    @Override
    public void freeze(long userId, Task task) {
        task.setStatus(Status.FREEZED);
        CompletableFuture.runAsync(() -> taskLogRepository.save(new TaskLog(userId, task, "status", task.getStatus().toString(), Status.FREEZED.toString())));
        taskRepository.save(task);
        logger.info("task %o has been freezed", task.getId());
    }

    @Override
    public PageInfo<Task> getCreated(Date from, Date to, long userId, Pageable pageable) {
        return new PageInfo<>(taskRepository.findCreated(userId, pageable));
    }

    @Override
    public PageInfo<Task> getCreatedBetween(Date startDate, Date endDate, long userId, Pageable pageable) {
        return new PageInfo<>(taskRepository.findCreatedBetween(startDate, endDate, userId, pageable));
    }

    @Override
    public PageInfo<Task> getAll(Pageable pageable)  {
        return new PageInfo<>(taskRepository.findAll(pageable));
    }


    @Override
    public void close(long userId, Task task, CloseReason reason) {
        task.setStatus(Status.CLOSED);
        task.setCloseReason(reason);
        CompletableFuture.runAsync(() -> {
            taskLogRepository.save(new TaskLog(userId, task, "status", task.getStatus().toString(), Status.CLOSED.toString()));
            taskLogRepository.save(new TaskLog(userId, task, "closeReason", CloseReason.NONE.toString(), reason.toString()));
        });
        taskRepository.save(task);
        logger.info("task %o has been closed by %o", task.getId(), userId);
    }
}
