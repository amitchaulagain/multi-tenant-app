package com.antuansoft.mongodb.domain;

import com.antuansoft.herospace.UserStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.awt.*;


@Document(collection = "user")
public class User {

    @Id
    private String id;
    private String name;
    private String surname;
    private int age;
    private String username;
    private String password;
    private String[] role;
    private String client_schema;
    private UserStatus status;

    public User() {
        super();
    }


    public User(String id, String name, String surname, String username,
                String password, String[] role) {
        super();
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getSurname() {
        return surname;
    }


    public void setSurname(String surname) {
        this.surname = surname;
    }


    public int getAge() {
        return age;
    }


    public void setAge(int age) {
        this.age = age;
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


    public String[] getRole() {
        return role;
    }


    public void setRole(String[] role) {
        this.role = role;
    }

    public String getClient_schema() {
        return client_schema;
    }

    public void setClient_schema
            (String client_schema) {
        this.client_schema = client_schema;
    }


    public UserStatus getStatus() {

        return  status;
    }
}
