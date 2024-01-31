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

/**
 * userのcontrollerクラス
 * 
 * @author hiraizumi
 */
@Controller
@RequestMapping({ "", "/" })
public class UserController {

    @Autowired
    private UserService service;

    /**
     * ユーザー新規作成画面への遷移
     * 
     * @param form  入力情報の保存用
     * @param model requestスコープ
     * @return 新規作成画面へ
     */
    @GetMapping("toRegister")
    public String toRegister(UserForm form, Model model) {
        // エラーがあればこれに入れて返す（初期は空）
        model.addAttribute("userForm", form);
        return "register";
    }

    /**
     * ユーザーの新規作成
     * 
     * @param form  入力情報の受け取り
     * @param rs    入力チェック
     * @param model requestスコープ
     * @return 成功ならconfirm画面へ、失敗なら新規作成画面に戻る
     */
    @PostMapping("register")
    public String registerUser(@Validated UserForm form, BindingResult rs, Model model) {
        // validationチェック
        if (rs.hasErrors()) {
            return toRegister(form, model);
        }
        // mailが重複していないか確認
        // していれば、エラーとして元の画面に戻る
        if (service.findUserByMail(form.getMail()) != null) {
            model.addAttribute("error", true);
            model.addAttribute("userForm", form);
            return "register";
        }
        // ユーザーの新規作成
        service.registerUser(form);
        return "confirm/user-register-confirm";
    }

}
