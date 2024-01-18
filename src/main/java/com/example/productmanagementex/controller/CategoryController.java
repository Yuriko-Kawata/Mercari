package com.example.productmanagementex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.productmanagementex.domain.User;
import com.example.productmanagementex.service.CategoryService;
import com.example.productmanagementex.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({ "", "/" })
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;

    @RequestMapping("categoryList")
    public String toCategoryList(@RequestParam(defaultValue = "1") int parentPage,
            @RequestParam(defaultValue = "1") int childPage,
            @RequestParam(defaultValue = "1") int grandPage) {
        if (session.getAttribute("categorySearchCondition") != null) {
            session.removeAttribute("categorySearchCondition");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserMail = authentication.getName();
        User user = userService.findUserByMail(currentUserMail);
        session.setAttribute("userName", user.getName());

        session.setAttribute("parentCategoryList", categoryService.findAllParentCategory(parentPage));
        session.setAttribute("childCategoryList", categoryService.findAllChildCategory(childPage));
        session.setAttribute("grandCategoryList", categoryService.findAllGrandCategory(grandPage));
        session.setAttribute("parentTotalPage", categoryService.totalParentPage());
        session.setAttribute("childTotalPage", categoryService.totalChildPage());
        session.setAttribute("grandTotalPage", categoryService.totalGrandPage());
        session.setAttribute("parentCurrentPage", parentPage);
        session.setAttribute("childCurrentPage", childPage);
        session.setAttribute("grandCurrentPage", grandPage);
        return "category-list";
    }

    @RequestMapping("searchCategory")
    public String toSearchCategory(String searchCategory, @RequestParam(defaultValue = "1") int parentPage,
            @RequestParam(defaultValue = "1") int childPage,
            @RequestParam(defaultValue = "1") int grandPage) {
        if (searchCategory == "") {
            return toCategoryList(parentPage, childPage, grandPage);
        }
        session.setAttribute("categorySearchCondition", searchCategory);
        session.setAttribute("parentCategoryList",
                categoryService.searchParentCategory(searchCategory, parentPage));
        session.setAttribute("parentTotalPage",
                categoryService.searchParentTotalPage(searchCategory));
        session.setAttribute("parentCurrentPage", parentPage);

        session.setAttribute("childCategoryList",
                categoryService.searchChildCategory(searchCategory, childPage));
        session.setAttribute("childTotalPage",
                categoryService.searchChildTotalPage(searchCategory));
        session.setAttribute("childCurrentPage", childPage);

        session.setAttribute("grandCategoryList",
                categoryService.searchGrandCategory(searchCategory, grandPage));
        session.setAttribute("grandTotalPage",
                categoryService.searchGrandTotalPage(searchCategory));
        session.setAttribute("grandCurrentPage", grandPage);

        return "category-list";
    }

    @RequestMapping("toParentSearch")
    public String toSearchParentPage(int parentPage) {
        String searchCondition = (String) session.getAttribute("categorySearchCondition");
        if (searchCondition == null) {
            int childPage = (int) session.getAttribute("childCurrentPage");
            int grandPage = (int) session.getAttribute("grandCurrentPage");
            return toCategoryList(parentPage, childPage, grandPage);
        }

        session.setAttribute("categoryList",
                categoryService.searchParentCategory(searchCondition, parentPage));
        session.setAttribute("parentTotalPage",
                categoryService.searchParentTotalPage(searchCondition));
        session.setAttribute("parentCurrentPage", parentPage);

        return "category-list";
    }

    @RequestMapping("toChildSearch")
    public String toSearchChildPage(int childPage) {
        String searchCondition = (String) session.getAttribute("categorySearchCondition");
        if (searchCondition == null) {
            int parentPage = (int) session.getAttribute("parentCurrentPage");
            int grandPage = (int) session.getAttribute("grandCurrentPage");
            return toCategoryList(parentPage, childPage, grandPage);
        }

        session.setAttribute("categoryList",
                categoryService.searchChildCategory(searchCondition, childPage));
        session.setAttribute("childTotalPage",
                categoryService.searchChildTotalPage(searchCondition));
        session.setAttribute("childCurrentPage", childPage);

        return "category-list";
    }

    @RequestMapping("toGrandSearch")
    public String toSearchGrandPage(int grandPage) {
        String searchCondition = (String) session.getAttribute("categorySearchCondition");
        if (searchCondition == null) {
            int parentPage = (int) session.getAttribute("parentCurrentPage");
            int childPage = (int) session.getAttribute("childCurrentPage");
            return toCategoryList(parentPage, childPage, grandPage);
        }

        session.setAttribute("categoryList",
                categoryService.searchGrandCategory(searchCondition, grandPage));
        session.setAttribute("grandTotalPage",
                categoryService.searchGrandTotalPage(searchCondition));
        session.setAttribute("grandCurrentPage", grandPage);

        return "category-list";
    }

    // @RequestMapping("detail")
    // public String detail(int id, Model model) {
    // model.addAttribute("item", itemService.findById(id));
    // return "detail";
    // }

    // @RequestMapping("toAdd")
    // public String toAddItem(ItemForm itemForm, CategoryForm categoryForm, Model
    // model) {
    // model.addAttribute("itemForm", itemForm);
    // model.addAttribute("categoryForm", categoryForm);
    // model.addAttribute("categoryList", categoryService.findAllUniqueCategory());

    // return "add";
    // }

    // @PostMapping("add")
    // public String addItem(@Validated ItemForm itemForm, BindingResult rsItem,
    // @Validated CategoryForm categoryForm,
    // BindingResult rsCategory, Model model) {
    // if (rsItem.hasErrors() || rsCategory.hasErrors()) {
    // return toAddItem(itemForm, categoryForm, model);
    // }

    // categoryService.checkCategory(categoryForm);
    // itemService.addItem(itemForm, categoryForm);

    // return "confirm/add-item-confirm";
    // }

    // @RequestMapping("toEdit")
    // public String toEditItem(int id, CategoryForm categoryForm, Model model) {
    // model.addAttribute("categoryForm", categoryForm);
    // model.addAttribute("categoryList", categoryService.findAllUniqueCategory());
    // model.addAttribute("itemData", itemService.findById(id));

    // return "edit";
    // }

    // @PostMapping("edit")
    // public String editItem(ItemForm itemForm, CategoryForm categoryForm, Model
    // model) {
    // categoryService.checkCategory(categoryForm);
    // itemService.editItem(itemForm, categoryForm);

    // model.addAttribute("itemId", itemForm.getId());
    // return "confirm/edit-item-confirm";
    // }

    // @PostMapping("delete")
    // public String deleteItem(int id, int page, Model model) {
    // itemService.delete(id);

    // return toSearchPage(page, model);
    // }

}
