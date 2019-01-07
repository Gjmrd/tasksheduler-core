package org.taskscheduler.services;

import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.VerificationToken;
import org.taskscheduler.domain.exceptions.InvalidVerificationTokenException;
import org.taskscheduler.rest.dto.JwtSignupRequest;

import java.util.List;


public interface UserService {
    User getUserById(long id) throws Exception;
    List<User> getAll() throws Exception;
    User getByEmail(String email);
    User getByUsername(String username);
    void changePassword(User user, String password);
    boolean passwordIsValid(User user, String password);
    boolean userExistsByUsername(String username) throws Exception;
    boolean userExistsByEmail(String email) throws Exception;
    User createUser(JwtSignupRequest request);
    VerificationToken createVerificationToken(User user);
    void sendVerificationToken(VerificationToken verificationToken);
    void confirmPasswordReset(String token, String newPassword) throws InvalidVerificationTokenException;
}
