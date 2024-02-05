package com.example.productmanagementex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String login(@RequestParam(name = "error", required = false) String error, Model model) {
        if (error != null) {
            // メッセージのキーを直接指定するのではなく、モデルにエラーフラグを設定
            model.addAttribute("loginError", true);
        }
        return "login";
    }
}
