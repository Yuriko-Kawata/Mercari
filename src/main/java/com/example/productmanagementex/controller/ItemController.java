package com.example.productmanagementex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.productmanagementex.form.SearchForm;
import com.example.productmanagementex.service.CategoryService;
import com.example.productmanagementex.service.ItemService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping({ "", "/" })
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private HttpSession session;

    @GetMapping({ "", "/" })
    public String index() {
        return "redirect:/itemList";
    }

    @RequestMapping("itemList")
    public String toItemList(@RequestParam(defaultValue = "1") int page, Model model) {
        if (session.getAttribute("searchCondition") != null) {
            session.removeAttribute("searchCondition");
        }
        model.addAttribute("itemList", itemService.findAllItems(page));
        int itemListSize = itemService.itemListSize();
        int totalPage = itemListSize / 30 + 1;
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());
        return "list";
    }

    @RequestMapping("search")
    public String toSearch(SearchForm form, Model model, @RequestParam(defaultValue = "1") int page) {
        session.setAttribute("searchCondition", form);
        model.addAttribute("itemList",
                itemService.searchItems(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory(), page));
        int itemListSize = itemService.searchItemsSize(form.getName(), form.getBrand(), form.getParentCategory(),
                form.getChildCategory(), form.getGrandCategory());
        int totalPage = itemListSize / 30 + 1;
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());

        return "list";
    }

    @RequestMapping("toSearch")
    public String toSearchPage(int page, Model model) {
        SearchForm form = (SearchForm) session.getAttribute("searchCondition");
        if (form == null) {
            return toItemList(page, model);
        }

        model.addAttribute("itemList",
                itemService.searchItems(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory(), page));
        int itemListSize = itemService.searchItemsSize(form.getName(), form.getBrand(), form.getParentCategory(),
                form.getChildCategory(), form.getGrandCategory());
        int totalPage = itemListSize / 30 + 1;
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());

        return "list";
    }

    @RequestMapping("detail")
    public String detail(int id, Model model) {
        model.addAttribute("item", itemService.findById(id));
        return "detail";
    }
}
