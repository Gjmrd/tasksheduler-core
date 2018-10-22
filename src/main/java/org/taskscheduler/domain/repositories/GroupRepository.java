package org.taskscheduler.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.taskscheduler.domain.entities.Group;

public interface GroupRepository extends PagingAndSortingRepository<Group, Integer> {
}
