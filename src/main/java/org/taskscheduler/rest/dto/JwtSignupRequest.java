package org.taskscheduler.rest.dto;

import java.io.Serializable;

public class JwtSignupRequest implements Serializable{
    private String username;
    private String password;
    private String lastName;
    private String firstName;
    private String email;

    public JwtSignupRequest() {
        super();
    }

    public JwtSignupRequest(String username,
                            String password,
                            String lastName,
                            String firstName,
                            String email) {
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
