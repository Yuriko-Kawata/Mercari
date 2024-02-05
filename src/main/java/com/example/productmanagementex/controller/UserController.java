package com.example.productmanagementex.controller;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LogManager.getLogger(UserController.class);

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
        logger.info("registerUser method started call: {}", form);

        // validationチェック
        if (rs.hasErrors()) {
            logger.warn("registerUser, validation error");
            return toRegister(form, model);
        }

        // password再入力のチェック
        if (!(form.getPassword().equals(form.getPasswordCheck()))) {
            String errorMessage = messageSource.getMessage("error.mail.not.match", null, Locale.getDefault());
            model.addAttribute("passwordError", errorMessage);
        }

        // mailが重複していないか確認
        // していれば、エラーとして元の画面に戻る
        if (service.findUserByMail(form.getMail()) != null) {
            String errorMessage = messageSource.getMessage("error.mail.duplicate", null, Locale.getDefault());
            model.addAttribute("mailError", errorMessage);
            model.addAttribute("userForm", form);

            logger.warn("registerUser, findUserByMail error");
            return "register";
        }
        // ユーザーの新規作成
        service.registerUser(form);

        logger.info("registerUser method finished");
        return "confirm/user-register-confirm";
    }

}
