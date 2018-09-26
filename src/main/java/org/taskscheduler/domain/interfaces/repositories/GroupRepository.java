package org.taskscheduler.domain.interfaces.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taskscheduler.domain.entities.Group;

public interface GroupRepository extends JpaRepository<Group, Integer>{
}
