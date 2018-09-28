package org.taskscheduler.domain.services;

import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.enums.CloseReason;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncTaskService {
    CompletableFuture<Task> getById(int id) throws Exception;
    CompletableFuture<List<Task>> getAll() throws Exception;
    CompletableFuture<Void> save(Task task);
    CompletableFuture<Void> delete(Task task);
    CompletableFuture<Void> close(Task task, CloseReason reason);
    CompletableFuture<List<Task>> getCreated(User user) throws Exception;
    CompletableFuture<List<Task>> getCreatedBetween(Date startDate, Date endDate, User user) throws Exception;
}
