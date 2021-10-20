package com.example.onlinestatus.model;

import java.sql.Date;

public class User {
    private Integer user_id;
    private String name;
    private Date date_of_birth;
    private Boolean active;

    public User(String name, Date date_of_birth) {
        this.name = name;
        this.date_of_birth = date_of_birth;
    }

    public User(String name, Date date_of_birth, Boolean active) {
        this.name = name;
        this.date_of_birth = date_of_birth;
        this.active = active;
    }

    public User(Integer user_id, String name, Date date_of_birth, Boolean active) {
        this.user_id = user_id;
        this.name = name;
        this.date_of_birth = date_of_birth;
        this.active = active;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public Boolean getActive() {
        return active;
    }
}
