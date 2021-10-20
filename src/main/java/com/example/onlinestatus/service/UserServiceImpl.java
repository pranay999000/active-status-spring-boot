package com.example.onlinestatus.service;

import com.example.onlinestatus.exception.AuthException;
import com.example.onlinestatus.model.User;
import com.example.onlinestatus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public Integer register(String name, Date date_of_birth) throws AuthException {
        return userRepository.create(name, date_of_birth);
    }

    @Override
    public Integer registerWithActive(String name, Date date_of_birth, Boolean active) throws AuthException {
        return userRepository.createWithActive(name, date_of_birth, active);
    }

    @Override
    public List<User> getActiveUsers(Boolean active) {
        return userRepository.getActiveUsers(active);
    }

    @Override
    public Boolean setActive(Integer id, Boolean active) throws AuthException {
        if (userRepository.findUser(id) > 0)
            return userRepository.setActive(id, active);
        else
            return null;
    }

    @Override
    public Boolean deleteUser(Integer id) {
        if (userRepository.findUser(id) > 0)
            return userRepository.deleteUser(id);
        else
            return false;
    }
}
