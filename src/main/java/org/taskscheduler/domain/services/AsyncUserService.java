package org.taskscheduler.domain.services;

import org.taskscheduler.domain.entities.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface AsyncUserService {
    CompletableFuture<User> getUserById(int id) throws Exception;
    CompletableFuture<List<User>> getAll() throws Exception;
    CompletableFuture<User> save(User user) throws Exception;
    CompletableFuture<Void> delete(User user);
    CompletableFuture<Void> changePassword(String password);
}
