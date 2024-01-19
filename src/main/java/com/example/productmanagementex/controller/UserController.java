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
@RequestMapping({ "", "/" })
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("toRegister")
    public String toRegister(UserForm form, Model model) {
        model.addAttribute("userForm", form);
        return "register";
    }

    @PostMapping("register")
    public String registerUser(@Validated UserForm form, BindingResult rs, Model model) {
        if (rs.hasErrors()) {
            return toRegister(form, model);
        }
        if (service.findUserByMail(form.getMail()) != null) {
            model.addAttribute("error", true);
            model.addAttribute("userForm", form);
            return "register";
        }
        service.registerUser(form);
        return "confirm/user-register-confirm";
    }

}
