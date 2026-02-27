package com.cbielaszczuk.crm.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserModel extends PersonModel {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public UserModel() {
        super();
    }

    public UserModel(Long id, String name, String email, String phone, String username, String password) {
        super(id, name, email, phone);
        this.username = username;
        this.password = password;
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
