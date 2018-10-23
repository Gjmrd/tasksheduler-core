package org.taskscheduler.services.impl;

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
import org.taskscheduler.services.TaskService;
import org.taskscheduler.rest.dto.TaskDto;


import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskLogRepository taskLogRepository;

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
    public PageInfo<Task> getUsersTasks(User user, Pageable pageable) {
        return new PageInfo<>(taskRepository.findUsersTasks(user, pageable));
    }

    @Override
    public Task createNew(User user, TaskDto taskDto) throws ExecutionException, InterruptedException {
        Task task = new Task();
        CompletableFuture future = CompletableFuture
                .supplyAsync(() -> userRepository.findByUsernameArray(taskDto.getExecutors()))
                .thenAccept(executors -> task.setExecutors(executors));
        task.setCaption(taskDto.getCaption());
        task.setDescription(taskDto.getDescription());
        task.setPriority(Priority.HIGHEST);
        task.setStatus(Status.ONGOING);
        task.setCreator(user);
        future.get();
        CompletableFuture.runAsync(() -> taskLogRepository.save(new TaskLog(user, task, "status", Status.NONE.toString(), Status.UNACCEPTED.toString() )));
        return taskRepository.save(task);
    }

    @Override
    public void freeze(User user, Task task) {
        task.setStatus(Status.FREEZED);
        CompletableFuture.runAsync(() -> taskLogRepository.save(new TaskLog(user, task, "status", task.getStatus().toString(), Status.FREEZED.toString())));
        taskRepository.save(task);
    }

    @Override
    public PageInfo<Task> getCreated(User user, Pageable pageable) {
        return new PageInfo<>(taskRepository.findCreated(user, pageable));
    }

    @Override
    public PageInfo<Task> getCreatedBetween(Date startDate, Date endDate, User user, Pageable pageable) {
        return new PageInfo<>(taskRepository.findCreatedBetween(startDate, endDate, user, pageable));
    }

    @Override
    public PageInfo<Task> getAll(Pageable pageable)  {
        return new PageInfo<>(taskRepository.findAll(pageable));
    }


    @Override
    public void close(User user, Task task, CloseReason reason) {
        task.setStatus(Status.CLOSED);
        task.setCloseReason(reason);
        CompletableFuture.runAsync(() -> {
            taskLogRepository.save(new TaskLog(user, task, "status", task.getStatus().toString(), Status.CLOSED.toString()));
            taskLogRepository.save(new TaskLog(user, task, "closeReason", CloseReason.NONE.toString(), reason.toString()));
        });

        taskRepository.save(task);
    }
}
