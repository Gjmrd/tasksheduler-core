package org.taskscheduler.domain.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.VerificationToken;
import org.taskscheduler.domain.entities.enums.AuthorityName;
import org.taskscheduler.domain.exceptions.InvalidVerificationTokenException;
import org.taskscheduler.domain.interfaces.repositories.AuthorityRepository;
import org.taskscheduler.domain.interfaces.repositories.UserRepository;
import org.taskscheduler.domain.interfaces.repositories.VerificationTokenRepository;
import org.taskscheduler.domain.services.UserService;

import java.util.*;


@Service
class UserServiceImpl implements UserService {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthorityRepository authorityRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private Calendar calendar;
    private JavaMailSender javaMailSender;

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
    public User getUserById(int id) {
        return userRepository.findById(id).orElseGet( null);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Async
    public User save(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    @Async
    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

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
    public User createUser(String username,
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
        return userRepository.save(user);
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
        return verificationTokenRepository.save(verificationToken);
    }

    @Override
    public void sendVerificationToken(VerificationToken verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(verificationToken.getUser().getEmail());
        message.setSubject("password reset verification token");
        message.setText("your verification token is : " + verificationToken.getToken());
        javaMailSender.send(message);
    }

    @Override
    public void confirmPasswordReset(String token, String newPassword) throws InvalidVerificationTokenException{
        VerificationToken verificationToken = verificationTokenRepository.getByToken(token);
        if (validateVerificationToken(verificationToken, token) == false) {
            throw new InvalidVerificationTokenException("Invalid token");
        }
        User user = verificationToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);

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