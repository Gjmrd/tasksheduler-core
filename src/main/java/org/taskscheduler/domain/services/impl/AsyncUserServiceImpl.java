package org.taskscheduler.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.interfaces.repositories.UserRepository;
import org.taskscheduler.domain.services.AsyncUserService;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
class AsyncUserServiceImpl implements AsyncUserService {


    private UserRepository userRepository;


    @Autowired
    public AsyncUserServiceImpl(UserRepository _userRepository) {
        userRepository = _userRepository;
    }

    @Override
    public CompletableFuture<User> getUserById(int id) {
        return CompletableFuture.completedFuture(userRepository.findById(id).orElseGet( null));
    }

    @Override
    @Async
    public CompletableFuture<List<User>> getAll() {
        return CompletableFuture.completedFuture(userRepository.findAll());
    }

    @Override
    @Async
    public CompletableFuture<User> save(User user) {
        return CompletableFuture.completedFuture(userRepository.saveAndFlush(user));
    }

    @Override
    public CompletableFuture<Void> delete(User user) {
        return  CompletableFuture.runAsync(() -> {
            userRepository.delete(user);
        });
    }

    @Override
    public CompletableFuture<Void> changePassword(String password) {
        return CompletableFuture.runAsync(() -> {});
    }

    //todo models for registration
    //todo spring security
    //todo spring jwt
    //todo task repositories and services


    public boolean userExists(int id) throws Exception{
        return this.getUserById(id).get() != null;
    }
}