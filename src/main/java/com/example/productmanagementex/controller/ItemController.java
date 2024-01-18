package com.example.productmanagementex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.productmanagementex.domain.User;
import com.example.productmanagementex.form.CategoryForm;
import com.example.productmanagementex.form.ItemForm;
import com.example.productmanagementex.form.SearchForm;
import com.example.productmanagementex.service.CategoryService;
import com.example.productmanagementex.service.ItemService;
import com.example.productmanagementex.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({ "", "/" })
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;

    @RequestMapping("itemList")
    public String toItemList(@RequestParam(defaultValue = "1") int page, Model model) {
        if (session.getAttribute("searchCondition") != null) {
            session.removeAttribute("searchCondition");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserMail = authentication.getName();
        User user = userService.findUserByMail(currentUserMail);
        session.setAttribute("userName", user.getName());

        model.addAttribute("itemList", itemService.findAllItems(page));
        model.addAttribute("totalPage", itemService.totalPage());
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());
        return "item-list";
    }

    @RequestMapping("search")
    public String toSearch(SearchForm form, Model model, @RequestParam(defaultValue = "1") int page) {
        session.setAttribute("searchCondition", form);
        model.addAttribute("itemList",
                itemService.searchItems(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory(), page));
        model.addAttribute("totalPage",
                itemService.searchTotalPage(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory()));
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());

        return "item-list";
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
        model.addAttribute("totalPage",
                itemService.searchTotalPage(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory()));
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());

        return "item-list";
    }

    @RequestMapping("detail")
    public String detail(int id, Model model) {
        model.addAttribute("item", itemService.findById(id));
        return "detail";
    }

    @RequestMapping("toAdd")
    public String toAddItem(ItemForm itemForm, CategoryForm categoryForm, Model model) {
        model.addAttribute("itemForm", itemForm);
        model.addAttribute("categoryForm", categoryForm);
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());

        return "add";
    }

    @PostMapping("add")
    public String addItem(@Validated ItemForm itemForm, BindingResult rsItem, @Validated CategoryForm categoryForm,
            BindingResult rsCategory, Model model) {
        if (rsItem.hasErrors() || rsCategory.hasErrors()) {
            return toAddItem(itemForm, categoryForm, model);
        }

        categoryService.checkCategory(categoryForm);
        itemService.addItem(itemForm, categoryForm);

        return "confirm/add-item-confirm";
    }

    @RequestMapping("toEdit")
    public String toEditItem(int id, CategoryForm categoryForm, Model model) {
        model.addAttribute("categoryForm", categoryForm);
        model.addAttribute("categoryList", categoryService.findAllUniqueCategory());
        model.addAttribute("itemData", itemService.findById(id));

        return "edit";
    }

    @PostMapping("edit")
    public String editItem(ItemForm itemForm, CategoryForm categoryForm, Model model) {
        categoryService.checkCategory(categoryForm);
        itemService.editItem(itemForm, categoryForm);

        model.addAttribute("itemId", itemForm.getId());
        return "confirm/edit-item-confirm";
    }

    @PostMapping("delete")
    public String deleteItem(int id, int page, Model model) {
        itemService.delete(id);

        return toSearchPage(page, model);
    }

}
