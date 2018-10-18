package org.taskscheduler.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.taskscheduler.domain.entities.PageInfo;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.domain.entities.enums.Priority;
import org.taskscheduler.domain.entities.enums.Status;
import org.taskscheduler.domain.interfaces.repositories.TaskRepository;
import org.taskscheduler.domain.interfaces.repositories.UserRepository;
import org.taskscheduler.domain.services.TaskService;
import org.taskscheduler.rest.controllers.dto.TaskDto;


import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
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
        return taskRepository.save(task);
    }

    @Override
    public void freeze(Task task) {
        task.setStatus(Status.FREEZED);
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
