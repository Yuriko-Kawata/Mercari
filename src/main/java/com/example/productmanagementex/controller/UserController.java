package com.example.productmanagementex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.productmanagementex.form.UserForm;
import com.example.productmanagementex.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("")
    public String index(UserForm form, Model model) {
        model.addAttribute("userForm", form);
        return "register";
    }

    @PostMapping("register")
    public String registerUser(@Validated UserForm form, BindingResult rs, Model model) {
        if (rs.hasErrors()) {
            return index(form, model);
        }

        service.registerUser(form);

        return "confirm";
    }
}
