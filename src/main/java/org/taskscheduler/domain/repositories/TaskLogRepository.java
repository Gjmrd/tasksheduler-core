package org.taskscheduler.domain.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.TaskLog;

import java.util.Date;

public interface TaskLogRepository extends PagingAndSortingRepository<TaskLog, Long> {
    Page<TaskLog> getAllByTask(Task task, Pageable pageable);

    @Query(value = "select t from TaskLog t where t.task = :task and t.date between :startDate and :endDate")
    Page<TaskLog> getAllByTask(@Param("task") Task task,
                            @Param("startDate") Date startDate,
                            @Param("endDate") Date endDate,
                            Pageable pageable);
}
