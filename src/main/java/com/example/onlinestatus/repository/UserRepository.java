package com.example.onlinestatus.repository;

import com.example.onlinestatus.model.User;

import java.sql.Date;
import java.util.List;

public interface UserRepository {

    Integer create(String name, Date date_of_birth);
    Integer createWithActive(String name, Date date_of_birth, Boolean active);

    List<User> getActiveUsers(Boolean active);

    Integer findUser(Integer id);
    Boolean setActive(Integer id, Boolean active);

    Boolean deleteUser(Integer id);
}
