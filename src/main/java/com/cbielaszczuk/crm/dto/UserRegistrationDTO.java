package com.cbielaszczuk.crm.dto;

/**
 * Data Transfer Object for user registration.
 */
public class UserRegistrationDTO {

    private String name;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String confirmPassword;

    /**
     * Constructs a UserRegistrationDTO.
     *
     * @param name            full name
     * @param email           email address
     * @param phone           phone number
     * @param username        desired username
     * @param password        password
     * @param confirmPassword confirmation of password
     */
    public UserRegistrationDTO() {}

    public UserRegistrationDTO(String name, String email, String phone, String username, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
