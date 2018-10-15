package org.taskscheduler.domain.services;

import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.VerificationToken;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.domain.exceptions.InvalidVerificationTokenException;
import org.taskscheduler.rest.controllers.dto.TaskDto;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TaskService {
    Task getById(long id) ;
    List<Task> getAll() throws Exception;
    Task createNew(User user, TaskDto taskDto) throws Exception;
    void save(Task task);
    void delete(Task task);
    void close(Task task, CloseReason reason);
    List<Task> getCreated(User user) throws Exception;
    List<Task> getCreatedBetween(Date startDate, Date endDate, User user) throws Exception;

}
