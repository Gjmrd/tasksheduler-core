package org.taskscheduler.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.interfaces.repositories.TaskRepository;
import org.taskscheduler.domain.services.AsyncTaskService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncTaskServiceImpl implements AsyncTaskService{

    private TaskRepository taskRepository;

    @Autowired
    public AsyncTaskServiceImpl(TaskRepository _taskRepository) {
        this.taskRepository = _taskRepository;
    }

    @Override
    @Async
    public CompletableFuture<Task> getById(int id) throws Exception {
        return CompletableFuture.completedFuture(taskRepository.findById(id).orElseGet(null));
    }

    @Override
    @Async
    public CompletableFuture<List<Task>> getAll() throws Exception {
        return CompletableFuture.completedFuture(taskRepository.findAll());
    }

    @Override
    @Async
    public CompletableFuture<Void> save(Task task) {
        return CompletableFuture.runAsync(() -> {
            taskRepository.save(task);
        });
    }

    @Override
    @Async
    public CompletableFuture<Void> delete(Task task) {
        return null;
    }
}
