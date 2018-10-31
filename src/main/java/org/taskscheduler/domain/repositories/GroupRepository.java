package org.taskscheduler.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.taskscheduler.domain.entities.Group;
import org.taskscheduler.domain.entities.User;

public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {

    @Query(value = "select g.members from Group g where g.id = :groupId")
    Page<User> getMembersByGroupId(@Param("groupId") long groupId, Pageable pageable);
}

