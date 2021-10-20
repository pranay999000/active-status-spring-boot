package com.example.onlinestatus.service;

import com.example.onlinestatus.exception.AuthException;
import com.example.onlinestatus.model.User;

import java.sql.Date;
import java.util.List;

public interface UserService {

    Integer register(String name, Date date_of_birth) throws AuthException;
    Integer registerWithActive(String name, Date date_of_birth, Boolean active) throws AuthException;

    List<User> getActiveUsers(Boolean active) throws AuthException;

    Boolean setActive(Integer id, Boolean active) throws AuthException;

    Boolean deleteUser(Integer id);

}
