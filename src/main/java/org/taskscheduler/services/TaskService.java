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
    PageInfo<Task> getUsersTasks(User user, Pageable pageable);
    Task createNew(User user, TaskDto taskDto) throws Exception;
    void close(Task task, CloseReason reason);
    void freeze(Task task);
    PageInfo<Task> getCreated(User user, Pageable pageable);
    PageInfo<Task> getCreatedBetween(Date startDate, Date endDate, User user, Pageable pageable) ;

}
