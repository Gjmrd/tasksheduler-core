package org.taskscheduler.services;


import org.springframework.data.domain.Pageable;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.rest.dto.TaskDto;
import org.taskscheduler.domain.entities.PageInfo;
import java.util.Date;


public interface TaskService {
    Task getById(long id) ;
    PageInfo<Task> getAll(Pageable pageable);
    PageInfo<Task> getUsersTasks(Date from, Date to, User user, Pageable pageable);
    Task createNew(long userId, TaskDto taskDto) throws Exception;
    void close(long userId, Task task, CloseReason reason);
    void freeze(long userId, Task task);
    PageInfo<Task> getCreated(Date from, Date to, long userId, Pageable pageable);
    PageInfo<Task> getCreatedBetween(Date startDate, Date endDate, long userId, Pageable pageable) ;
    PageInfo<User> getExecutorsByTaskId(long taskId, Pageable pageable);

}
