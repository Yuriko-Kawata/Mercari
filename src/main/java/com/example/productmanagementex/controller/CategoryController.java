package com.example.productmanagementex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.productmanagementex.form.CategoryForm;
import com.example.productmanagementex.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({ "", "/" })
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private HttpSession session;

    @RequestMapping("categoryList")
    public String toCategoryList(@RequestParam(defaultValue = "1") int parentPage,
            @RequestParam(defaultValue = "1") int childPage,
            @RequestParam(defaultValue = "1") int grandPage, Model model) {
        if (session.getAttribute("categorySearchCondition") != null) {
            session.removeAttribute("categorySearchCondition");
        }
        model.addAttribute("searchCondition", "");
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
            @RequestParam(defaultValue = "1") int grandPage, Model model) {
        if (searchCategory == "") {
            return toCategoryList(parentPage, childPage, grandPage, model);
        }
        session.setAttribute("categorySearchCondition", searchCategory);
        model.addAttribute("searchCondition", searchCategory);
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
    public String toSearchParentPage(int parentPage, Model model) {
        String searchCondition = (String) session.getAttribute("categorySearchCondition");
        if (searchCondition == null) {
            int childPage = (int) session.getAttribute("childCurrentPage");
            int grandPage = (int) session.getAttribute("grandCurrentPage");
            return toCategoryList(parentPage, childPage, grandPage, model);
        }
        model.addAttribute("searchCondition", searchCondition);
        session.setAttribute("parentCategoryList",
                categoryService.searchParentCategory(searchCondition, parentPage));
        session.setAttribute("parentTotalPage",
                categoryService.searchParentTotalPage(searchCondition));
        session.setAttribute("parentCurrentPage", parentPage);

        return "category-list";
    }

    @RequestMapping("toChildSearch")
    public String toSearchChildPage(int childPage, Model model) {
        String searchCondition = (String) session.getAttribute("categorySearchCondition");
        if (searchCondition == null) {
            int parentPage = (int) session.getAttribute("parentCurrentPage");
            int grandPage = (int) session.getAttribute("grandCurrentPage");
            return toCategoryList(parentPage, childPage, grandPage, model);
        }
        model.addAttribute("searchCondition", searchCondition);
        session.setAttribute("childCategoryList",
                categoryService.searchChildCategory(searchCondition, childPage));
        session.setAttribute("childTotalPage",
                categoryService.searchChildTotalPage(searchCondition));
        session.setAttribute("childCurrentPage", childPage);

        return "category-list";
    }

    @RequestMapping("toGrandSearch")
    public String toSearchGrandPage(int grandPage, Model model) {
        String searchCondition = (String) session.getAttribute("categorySearchCondition");
        if (searchCondition == null) {
            int parentPage = (int) session.getAttribute("parentCurrentPage");
            int childPage = (int) session.getAttribute("childCurrentPage");
            return toCategoryList(parentPage, childPage, grandPage, model);
        }

        model.addAttribute("searchCondition", searchCondition);
        session.setAttribute("grandCategoryList",
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

    @RequestMapping("toAddCategory")
    public String toAddCategory(CategoryForm categoryForm, Model model) {
        model.addAttribute("categoryForm", categoryForm);
        model.addAttribute("categoryList", categoryService.findAllCategory());

        return "add-category";
    }

    @PostMapping("addCategory")
    public String addCategory(CategoryForm categoryForm, Model model) {
        if (categoryService.checkCategory(categoryForm) != 0) {
            model.addAttribute("error", true);
            return toAddCategory(categoryForm, model);
        }

        categoryService.insertCategory(categoryForm);
        return "confirm/add-category-confirm";
    }

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
