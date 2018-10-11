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
import org.taskscheduler.domain.exceptions.AuthenticationException;
import org.taskscheduler.domain.exceptions.RegistrationException;
import org.taskscheduler.domain.services.UserService;
import org.taskscheduler.rest.controllers.dto.JwtAuthenticationResponse;
import org.taskscheduler.rest.controllers.dto.JwtAuthenticationRequest;
import org.taskscheduler.rest.controllers.dto.JwtSignUpResponse;
import org.taskscheduler.rest.controllers.dto.JwtSignupRequest;
import org.taskscheduler.security.JwtTokenUtil;
import org.taskscheduler.security.JwtUser;


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

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signup(@RequestBody JwtSignupRequest request) throws Exception{
        // checking if username is already in use
        if (userService.userExistsByUsername(request.getUsername())) {
            throw new RegistrationException("User with this nickname already exists", new HTTPException(400));
        }
        if (userService.userExistsByEmail(request.getEmail())) {
            throw new RegistrationException("This email is already in user", new HTTPException(400));
        }
        User user = userService.createUser(request.getUsername(),
                                                request.getPassword(),
                                                request.getLastName(),
                                                request.getFirstName(),
                                                request.getEmail());

        authenticate(request.getUsername(), request.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtSignUpResponse(token, userDetails));
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public String changePassword(@AuthenticationPrincipal User user,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword) throws Exception{

        if (userService.passwordIsValid(user, oldPassword)) {
            userService.changePassword(user, newPassword);
            return "ok";
        } else
            throw new AuthenticationException("Wrong password", new HTTPException(400));
    }


    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
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

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
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
