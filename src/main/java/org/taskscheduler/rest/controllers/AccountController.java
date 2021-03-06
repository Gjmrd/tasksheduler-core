package org.taskscheduler.rest.controllers;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.VerificationToken;
import org.taskscheduler.domain.exceptions.AuthenticationException;
import org.taskscheduler.domain.exceptions.EntityNotFoundException;
import org.taskscheduler.domain.exceptions.InvalidVerificationTokenException;
import org.taskscheduler.domain.exceptions.RegistrationException;
import org.taskscheduler.domain.security.JwtUserFactory;
import org.taskscheduler.domain.services.UserService;
import org.taskscheduler.rest.dto.JwtAuthenticationResponse;
import org.taskscheduler.rest.dto.JwtAuthenticationRequest;
import org.taskscheduler.rest.dto.JwtSignUpResponse;
import org.taskscheduler.rest.dto.JwtSignupRequest;
import org.taskscheduler.domain.security.JwtTokenUtil;
import org.taskscheduler.domain.security.JwtUser;


@RestController
public class AccountController {


    private String tokenHeader;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @Autowired
    public AccountController(@Value("${jwt.header}") String tokenHeader,
                             AuthenticationManager authenticationManager,
                             JwtTokenUtil jwtTokenUtil,
                             @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService,
                             UserService userService) {
        this.tokenHeader = tokenHeader;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());


        User user = userService.getByUsername(authenticationRequest.getUsername());
        final JwtUser userDetails = JwtUserFactory.create(user);//userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @GetMapping("/authtest")
    public ResponseEntity<?> test(@AuthenticationPrincipal JwtUser user) {
        return ResponseEntity.ok(user);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody JwtSignupRequest request) throws Exception{
        // checking if username is already in use
        if (userService.userExistsByUsername(request.getUsername())) {
            throw new RegistrationException("User with this nickname already exists", new HTTPException(400));
        }
        if (userService.userExistsByEmail(request.getEmail())) {
            throw new RegistrationException("This email is already in user", new HTTPException(400));
        }
        User user = userService.createUser(request);
        authenticate(request.getUsername(), request.getPassword());

        final JwtUser userDetails = JwtUserFactory.create(user);

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtSignUpResponse(token, userDetails));
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword) throws Exception{

        User user = userService.getByUsername(userDetails.getUsername());
        if (userService.passwordIsValid(user, oldPassword)) {
            userService.changePassword(user, newPassword);
            return ResponseEntity.ok("ok");
        } else
            throw new AuthenticationException("Wrong password", new HTTPException(400));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email) {
        User user = userService.getByEmail(email);
        if (user == null)
            throw new EntityNotFoundException(String.format("user with email %s not found",  email));
        VerificationToken verificationToken = userService.createVerificationToken(user);
        userService.sendVerificationToken(verificationToken);
        return ResponseEntity.ok("check your email");
    }

    @GetMapping(value = "/password/reset")
    public ResponseEntity<?> confirmPasswordReset(@RequestParam("token") String token,
                                                  @RequestParam("newPassword") String newPassword) {
        try {
            userService.confirmPasswordReset(token, newPassword);
        }
        catch (InvalidVerificationTokenException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("ok");
    }


    @GetMapping("${jwt.route.authentication.refresh}")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        if (jwtTokenUtil.canTokenBeRefreshed(token, jwtTokenUtil.getLastPasswordResetDateFromToken(token))) {
            //return ResponseEntity.ok("1234");
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }


    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
}
