package com.example.productmanagementex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.productmanagementex.service.CategoryService;
import com.example.productmanagementex.service.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping({ "", "/" })
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("itemList")
    public String toItemList(Model model) {
        model.addAttribute("itemList", itemService.findAllItems());
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());
        return "list";
    }

    @PostMapping("searchItemList")
    public String toSearchItemList(String name, String brand, String parentCategory, String childCategory,
            String grandCategory, Model model) {
        model.addAttribute("itemList",
                itemService.searchItems(name, brand, parentCategory, childCategory, grandCategory));
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());

        return "list";
    }

}
