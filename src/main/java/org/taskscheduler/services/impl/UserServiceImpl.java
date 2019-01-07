package org.taskscheduler.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.VerificationToken;
import org.taskscheduler.domain.entities.enums.AuthorityName;
import org.taskscheduler.domain.exceptions.InvalidVerificationTokenException;
import org.taskscheduler.domain.repositories.AuthorityRepository;
import org.taskscheduler.domain.repositories.UserRepository;
import org.taskscheduler.domain.repositories.VerificationTokenRepository;
import org.taskscheduler.services.UserService;
import org.taskscheduler.rest.dto.JwtSignupRequest;

import java.util.*;


@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthorityRepository authorityRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private Calendar calendar;
    private JavaMailSender javaMailSender;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthorityRepository authorityRepository,
                           JavaMailSender javaMailSender,
                           VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.calendar = Calendar.getInstance();
        this.javaMailSender = javaMailSender;
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseGet( null);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        logger.info("user %s has been changed password",  user.getUsername());
    }

    @Override
    public boolean passwordIsValid(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean userExistsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User createUser(JwtSignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setEnabled(true);
        user.setAuthorities(Collections.singletonList(authorityRepository.findByName(AuthorityName.ROLE_USER)));
        userRepository.save(user);
        logger.info("user %s has been created", user.getUsername());
        return user;
    }

    @Override
    public VerificationToken createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        calendar.add(Calendar.MINUTE, VerificationToken.EXPIRE_TIME);
        Date expiryDate = calendar.getTime();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiresAt(expiryDate);
        verificationTokenRepository.save(verificationToken);
        logger.info("verification token for user %s has been created", user.getUsername());
        return verificationToken;
    }

    @Override
    public void sendVerificationToken(VerificationToken verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(verificationToken.getUser().getEmail());
        message.setSubject("password reset verification token");
        message.setText("your verification token is : " + verificationToken.getToken());
        javaMailSender.send(message);
        logger.info("verification token %o has been sent to %s", verificationToken.getId(), verificationToken.getUser().getUsername());
    }

    @Override
    public void confirmPasswordReset(String token, String newPassword) throws InvalidVerificationTokenException{
        VerificationToken verificationToken = verificationTokenRepository.getByToken(token);
        if (!validateVerificationToken(verificationToken, token)) {
            throw new InvalidVerificationTokenException("Invalid token");
        }
        User user = verificationToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        logger.info("user %s has confirmed password reset",  user.getUsername());

    }

    private boolean validateVerificationToken(VerificationToken verificationToken, String token) {
        if (verificationToken == null)
            return false;
        if (verificationToken.getToken() != token)
            return false;
        if (verificationToken.getExpiresAt().after(calendar.getTime()))
            return false;
        return true;
    }
}