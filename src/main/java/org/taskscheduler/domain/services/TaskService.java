package org.taskscheduler.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Task> getAll(Pageable pageable);
    Page<Task> getUsersTasks(User user, Pageable pageable);
    Task createNew(User user, TaskDto taskDto) throws Exception;
    void save(Task task);
    void delete(Task task);
    void close(Task task, CloseReason reason);
    void freeze(Task task);
    Page<Task> getCreated(User user, Pageable pageable);
    Page<Task> getCreatedBetween(Date startDate, Date endDate, User user, Pageable pageable) ;

}
