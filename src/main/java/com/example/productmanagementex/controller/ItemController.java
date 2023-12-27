package com.example.productmanagementex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.productmanagementex.service.ItemService;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping({ "", "/" })
public class ItemController {

    @Autowired
    private ItemService service;

    @GetMapping("itemList")
    public String toItemList(Model model) {
        model.addAttribute("itemList", service.findAllItems());
        return "list";
    }

}
