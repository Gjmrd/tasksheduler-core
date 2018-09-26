package org.taskscheduler.domain.interfaces.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer>{
    @Query(value = "select t from Task t where t.getCreatorId = :user and t.createdAt between :startDate and :endDate")
    List<Task> findCreated(Date startDate, Date endDate, User user);


    //TODO test this method



}
