package com.cbielaszczuk.crm.dto;

/**
 * Data Transfer Object for user-related operations (CRUD).
 */
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String username;
    private String password;

    /**
     * Constructs a UserDTO with all fields.
     *
     * @param id        unique identifier
     * @param name      full name
     * @param email     email address
     * @param phone     phone number
     * @param username  login username
     * @param password  login password
     */
    public UserDTO(Long id, String name, String email, String phone, String username, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
