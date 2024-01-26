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

import com.example.productmanagementex.domain.Category;
import com.example.productmanagementex.domain.Item;
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
        SearchForm form = new SearchForm();
        if (session.getAttribute("form") != null) {
            session.removeAttribute("form");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserMail = authentication.getName();
        User user = userService.findUserByMail(currentUserMail);
        session.setAttribute("userName", user.getName());

        model.addAttribute("searchCondition", form);
        model.addAttribute("itemList", itemService.findAllItems(page));

        int totalItem = itemService.totalItem();
        int totalPage = 0;
        if (totalItem % 30 == 0) {
            totalPage = totalItem / 30;
        } else {
            totalPage = totalItem / 30 + 1;
        }

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalItemCount", totalItem);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllCategory());
        return "item-list";
    }

    @RequestMapping("search")
    public String toSearch(SearchForm form, Model model, @RequestParam(defaultValue = "1") int page) {
        session.setAttribute("form", form);

        model.addAttribute("searchCondition", form);
        model.addAttribute("itemList",
                itemService.searchItems(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory(), page));
        int totalItem = itemService.searchTotalItem(form.getName(), form.getBrand(), form.getParentCategory(),
                form.getChildCategory(), form.getGrandCategory());

        int totalPage = 0;
        if (totalItem % 30 == 0) {
            totalPage = totalItem / 30;
        } else {
            totalPage = totalItem / 30 + 1;
        }

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalItemCount", totalItem);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllCategory());

        return "item-list";
    }

    @RequestMapping("toSearch")
    public String toSearchPage(int page, Model model) {
        SearchForm form = (SearchForm) session.getAttribute("form");
        if (form == null) {
            return toItemList(page, model);
        }

        model.addAttribute("searchCondition", form);
        model.addAttribute("itemList",
                itemService.searchItems(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory(), page));
        int totalItem = itemService.searchTotalItem(form.getName(), form.getBrand(), form.getParentCategory(),
                form.getChildCategory(), form.getGrandCategory());

        int totalPage = 0;
        if (totalItem % 30 == 0) {
            totalPage = totalItem / 30;
        } else {
            totalPage = totalItem / 30 + 1;
        }
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalItemCount", totalItem);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllCategory());

        return "item-list";
    }

    @RequestMapping("detail")
    public String detail(int id, Model model) {
        model.addAttribute("item", itemService.findById(id));
        return "detail";
    }

    @RequestMapping("toAdd")
    public String toAddItem(ItemForm itemForm, Model model) {
        model.addAttribute("itemForm", itemForm);
        model.addAttribute("categoryList", categoryService.findAllCategory());

        return "add";
    }

    @PostMapping("add")
    public String addItem(@Validated ItemForm itemForm, BindingResult rs, CategoryForm categoryForm, Model model) {
        if (rs.hasErrors()) {
            return toAddItem(itemForm, model);
        }

        itemService.addItem(itemForm, categoryForm);
        return "confirm/add-item-confirm";
    }

    @RequestMapping("toEdit")
    public String toEditItem(int id, CategoryForm categoryForm, Model model) {
        model.addAttribute("categoryForm", categoryForm);
        model.addAttribute("categoryList", categoryService.findAllCategory());
        Item item = itemService.findById(id);
        String originalParentCategory = null;
        String originalChildCategory = null;
        String originalGrandCategory = null;
        for (Category category : item.getCategory()) {
            if (category.getParentId() != 0) {
                if (category.getNameAll() != null) {
                    originalGrandCategory = category.getName();
                } else {
                    originalChildCategory = category.getName();
                }
            } else {
                originalParentCategory = category.getName();
            }
        }
        model.addAttribute("originalParentCategory", originalParentCategory);
        model.addAttribute("originalChildCategory", originalChildCategory);
        model.addAttribute("originalGrandCategory", originalGrandCategory);
        model.addAttribute("itemData", item);

        return "edit";
    }

    @PostMapping("edit")
    public String editItem(ItemForm itemForm, CategoryForm categoryForm, Model model) {
        if (categoryForm.getParentCategory() == "" && categoryForm.getChildCategory() == null
                && categoryForm.getGrandCategory() == null) {
            Item item = itemService.findById(itemForm.getId());
            String originalParentCategory = null;
            String originalChildCategory = null;
            String originalGrandCategory = null;
            for (Category category : item.getCategory()) {
                if (category.getParentId() != 0) {
                    if (category.getNameAll() != null) {
                        originalGrandCategory = category.getName();
                    } else {
                        originalChildCategory = category.getName();
                    }
                } else {
                    originalParentCategory = category.getName();
                }
            }
            categoryForm.setParentCategory(originalParentCategory);
            categoryForm.setChildCategory(originalChildCategory);
            categoryForm.setGrandCategory(originalGrandCategory);

            itemService.editItem(itemForm, categoryForm);
            model.addAttribute("itemId", itemForm.getId());
            return "confirm/edit-item-confirm";

        } else if (categoryForm.getParentCategory() != "" && categoryForm.getChildCategory() == "") {
            model.addAttribute("choiceError", model);
            return toEditItem(itemForm.getId(), categoryForm, model);
        } else if (categoryForm.getParentCategory() != "" && categoryForm.getChildCategory() != ""
                && categoryForm.getGrandCategory() == "") {
            model.addAttribute("choiceError", model);
            return toEditItem(itemForm.getId(), categoryForm, model);
        }

        itemService.editItem(itemForm, categoryForm);
        model.addAttribute("itemId", itemForm.getId());
        return "confirm/edit-item-confirm";
    }

    @RequestMapping("delete")
    public String deleteItem(int id, Model model) {
        itemService.delete(id);
        return "confirm/delete-item-confirm";
    }

}
