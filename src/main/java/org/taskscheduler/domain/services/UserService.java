package org.taskscheduler.domain.services;

import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.VerificationToken;
import org.taskscheduler.domain.exceptions.InvalidVerificationTokenException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface UserService {
    User getUserById(int id) throws Exception;
    List<User> getAll() throws Exception;
    User save(User user) throws Exception;
    User getByEmail(String email);
    void delete(User user);
    void changePassword(User user, String password);
    boolean passwordIsValid(User user, String password);
    boolean userExistsByUsername(String username) throws Exception;
    boolean userExistsByEmail(String email) throws Exception;
    User createUser(String lastName,
                                       String firstName,
                                       String email,
                                       String username,
                                       String password);
    VerificationToken createVerificationToken(User user);
    void sendVerificationToken(VerificationToken verificationToken);
    void confirmPasswordReset(String token, String newPassword) throws InvalidVerificationTokenException;
}
