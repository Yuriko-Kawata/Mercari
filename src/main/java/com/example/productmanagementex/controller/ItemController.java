package com.example.productmanagementex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.productmanagementex.service.CategoryService;
import com.example.productmanagementex.service.ItemService;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping({ "", "/" })
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping({ "", "/" })
    public String index() {
        return "redirect:/itemList";
    }

    @RequestMapping("itemList")
    public String toItemList(@RequestParam(defaultValue = "1") int page, Model model) {
        model.addAttribute("itemList", itemService.findAllItems(page));
        int itemListSize = itemService.itemListSize();
        int totalPage = itemListSize / 30 + 1;
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());
        return "list";
    }

    @RequestMapping("searchItemList")
    public String toSearchItemList(String name, String brand, String parentCategory, String childCategory,
            String grandCategory, Model model, @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("itemList",
                itemService.searchItems(name, brand, parentCategory, childCategory, grandCategory, page));
        int itemListSize = itemService.searchItemsSize(name, brand, parentCategory, childCategory, grandCategory);
        int totalPage = itemListSize / 30 + 1;
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());

        return "list";
    }

}
