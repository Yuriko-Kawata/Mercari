package com.example.productmanagementex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * login用のcontrollerクラス
 * 
 * @author hiraizumi
 */
@Controller
public class LoginController {

    /**
     * ログイン画面への遷移
     * 
     * @return ログイン画面へ
     */
    @GetMapping({ "", "/", "/login" })
    public String login() {
        return "login";
    }

}
