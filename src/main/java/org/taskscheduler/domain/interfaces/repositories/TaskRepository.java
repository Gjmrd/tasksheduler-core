package org.taskscheduler.domain.interfaces.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.taskscheduler.domain.entities.PageInfo;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
    @Query(value = "select t from Task t where t.creator = :user and t.createdAt between :startDate and :endDate")
    Page<Task> findCreatedBetween(@Param("startDate") Date startDate,
                                  @Param("endDate") Date endDate,
                                  @Param("user") User user,
                                  Pageable pageable);

    @Query(value = "select t from Task t where t.creator = :user")
    Page<Task> findCreated(@Param("user") User user, Pageable pageable);

    @Query(value = "select t from Task t where (t.creator = :user or :user in elements(t.executors))")
    Page<Task> findUsersTasks(@Param("user") User user, Pageable pageable);

}
