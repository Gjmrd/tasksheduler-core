package org.taskscheduler.domain.interfaces.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>{
    @Query(value = "select t from Task t where t.creator = :user and t.createdAt between :startDate and :endDate")
    List<Task> findCreatedBetween(@Param("startDate") Date startDate,
                                  @Param("endDate") Date endDate,
                                  @Param("user") User user);

    @Query(value = "select t from Task t where t.creator = :user")
    List<Task> findCreated(@Param("user") User user);



}
