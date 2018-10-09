package org.taskscheduler.domain.interfaces.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.taskscheduler.domain.entities.Authority;
import org.taskscheduler.domain.entities.enums.AuthorityName;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    @Query("select a from Authority  a where a.name = :name")
    Authority findByName(@Param("name") AuthorityName name);
}
