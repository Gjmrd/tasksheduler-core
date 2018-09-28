package org.taskscheduler.domain.interfaces.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taskscheduler.domain.entities.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}