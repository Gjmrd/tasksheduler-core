package org.taskscheduler.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.taskscheduler.domain.entities.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    @Query("select u from User u where u.username in :usernames")
    List<User> findByUsernameArray(@Param("usernames") String[] usernames);
}