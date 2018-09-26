package org.taskscheduler.domain.services;

import org.taskscheduler.domain.entities.Task;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncTaskService {
    CompletableFuture<Task> getById(int id) throws Exception;
    CompletableFuture<List<Task>> getAll() throws Exception;
    CompletableFuture<Void> save(Task task);
    CompletableFuture<Void> delete(Task task);
}
