package org.taskscheduler.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.taskscheduler.domain.entities.Authority;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.enums.AuthorityName;
import org.taskscheduler.domain.interfaces.repositories.AuthorityRepository;
import org.taskscheduler.domain.interfaces.repositories.UserRepository;
import org.taskscheduler.domain.services.AsyncUserService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
class AsyncUserServiceImpl implements AsyncUserService {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthorityRepository authorityRepository;

    @Autowired
    public AsyncUserServiceImpl(UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
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
    @Async
    public CompletableFuture<Void> delete(User user) {
        return  CompletableFuture.runAsync(() -> {
            userRepository.delete(user);
        });
    }

    @Override
    @Async
    public CompletableFuture<Void> changePassword(User user, String password) {
        return CompletableFuture.runAsync(() -> {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        });
    }

    @Override
    @Async
    public CompletableFuture<Boolean> passwordIsValid(User user, String password) {
        return CompletableFuture.completedFuture(passwordEncoder.matches(password, user.getPassword()));
    }

    @Override
    @Async
    public CompletableFuture<Boolean> userExistsByUsername(String username) {
        return CompletableFuture.completedFuture(userRepository.findByUsername(username) != null);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> userExistsByEmail(String email) {
        return CompletableFuture.completedFuture(userRepository.findByEmail(email) != null);
    }

    @Override
    @Async
    public CompletableFuture<User> createUser(String username,
                                              String password,
                                              String lastName,
                                              String firstName,
                                              String email) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEnabled(true);
        user.setAuthorities(Arrays.asList(authorityRepository.findByName(AuthorityName.ROLE_USER)));
        return CompletableFuture.completedFuture(userRepository.save(user));
    }
}