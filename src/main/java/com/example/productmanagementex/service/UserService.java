package com.example.productmanagementex.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.User;
import com.example.productmanagementex.form.UserForm;
import com.example.productmanagementex.repository.UserRepository;

@Transactional
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void registerUser(UserForm form) {
        User user = new User();
        BeanUtils.copyProperties(form, user);

        repository.insertUser(user.getName(), user.getMail(), user.getPassword());
    }

    public User checkUser(UserForm form) {
        User user = repository.checkUser(form.getMail(), form.getPassword());
        return user;
    }
}
