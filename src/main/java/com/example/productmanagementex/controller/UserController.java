package com.example.productmanagementex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.productmanagementex.domain.User;
import com.example.productmanagementex.form.UserForm;
import com.example.productmanagementex.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private HttpSession session;

    @GetMapping({ "", "/" })
    public String index() {
        return "login";
    }

    @PostMapping("login")
    public String login(UserForm userForm, Model model) {
        User user = new User();
        user = service.checkUser(userForm);
        if (user == null) {
            model.addAttribute("checkUser", false);
            return "login";
        }

        session.setAttribute("user", user);
        return "redirect:/itemList";
    }

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

        service.registerUser(form);
        return "confirm";
    }

}
