package org.taskscheduler.domain.services;

import org.taskscheduler.domain.entities.User;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface AsyncUserService {
    CompletableFuture<User> getUserById(int id) throws Exception;
    CompletableFuture<List<User>> getAll() throws Exception;
    CompletableFuture<User> save(User user) throws Exception;
    CompletableFuture<Void> delete(User user);
    CompletableFuture<Void> changePassword(String password);
    CompletableFuture<Boolean> userExistsByUsername(String username) throws Exception;
    CompletableFuture<Boolean> userExistsByEmail(String email) throws Exception;
    CompletableFuture<User> createUser(String lastName,
                                       String firstName,
                                       String email,
                                       String username,
                                       String password);
}
