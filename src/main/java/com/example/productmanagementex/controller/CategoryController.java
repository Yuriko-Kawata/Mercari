package com.example.productmanagementex.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.productmanagementex.form.CategoryForm;
import com.example.productmanagementex.service.CategoryService;
import com.example.productmanagementex.service.ItemService;

import jakarta.servlet.http.HttpSession;

/**
 * categoryのcontorollerクラス
 * 
 * @author hiraizumi
 */
@Controller
@RequestMapping({ "", "/" })
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private HttpSession session;

    private static final Logger logger = LogManager.getLogger(CategoryController.class);

    /**
     * カテゴリ一覧の表示
     * 
     * @param parentPage 親カテゴリのcurrent page
     * @param childPage  子カテゴリのcurrent page
     * @param grandPage  孫カテゴリのcurrent page
     * @param model      requestスコープ
     * @return カテゴリ一覧へ
     */
    @RequestMapping("categoryList")
    public String toCategoryList(@RequestParam(defaultValue = "1") int parentPage,
            @RequestParam(defaultValue = "1") int childPage,
            @RequestParam(defaultValue = "1") int grandPage, Model model) {
        // sessionに検索条件があれば削除
        if (session.getAttribute("categorySearchCondition") != null) {
            session.removeAttribute("categorySearchCondition");
        }
        // 検索条件の作成（初期は””）
        model.addAttribute("searchCondition", "");
        // 親カテゴリリストの取得
        session.setAttribute("parentCategoryList", categoryService.findAllParentCategory(parentPage));
        // 子カテゴリリストの取得
        session.setAttribute("childCategoryList", categoryService.findAllChildCategory(childPage));
        // 孫カテゴリリストの取得
        session.setAttribute("grandCategoryList", categoryService.findAllGrandCategory(grandPage));

        // 親カテゴリのトータル件数の取得
        session.setAttribute("parentTotalPage", categoryService.totalParentPage());
        // 子カテゴリのトータル件数の取得
        session.setAttribute("childTotalPage", categoryService.totalChildPage());
        // 孫カテゴリのトータル件数の取得
        session.setAttribute("grandTotalPage", categoryService.totalGrandPage());

        // 親カテゴリのcurrent pageの更新
        session.setAttribute("parentCurrentPage", parentPage);
        // 子カテゴリのcurrent pageの更新
        session.setAttribute("childCurrentPage", childPage);
        // 孫カテゴリのcurrent pageの更新
        session.setAttribute("grandCurrentPage", grandPage);

        return "category-list";
    }

    /**
     * カテゴリ検索
     * 
     * @param searchCategory 検索条件
     * @param parentPage     親カテゴリのcurrent page
     * @param childPage      子カテゴリのcurrent page
     * @param grandPage      孫カテゴリのcurrent page
     * @param model          requestスコープ
     * @return カテゴリ一覧へ
     */
    @RequestMapping("searchCategory")
    public String toSearchCategory(String searchCategory, @RequestParam(defaultValue = "1") int parentPage,
            @RequestParam(defaultValue = "1") int childPage,
            @RequestParam(defaultValue = "1") int grandPage, Model model) {
        logger.info("searchCategory method started");

        // 検索条件が””であれば、ページを更新して一覧表示へ
        if (searchCategory == "") {
            return toCategoryList(parentPage, childPage, grandPage, model);
        }
        session.setAttribute("categorySearchCondition", searchCategory);
        model.addAttribute("searchCondition", searchCategory);

        session.setAttribute("parentCategoryList",
                categoryService.searchParentCategory(searchCategory, parentPage));
        session.setAttribute("childCategoryList",
                categoryService.searchChildCategory(searchCategory, childPage));
        session.setAttribute("grandCategoryList",
                categoryService.searchGrandCategory(searchCategory, grandPage));

        session.setAttribute("parentTotalPage",
                categoryService.searchParentTotalPage(searchCategory));
        session.setAttribute("childTotalPage",
                categoryService.searchChildTotalPage(searchCategory));
        session.setAttribute("grandTotalPage",
                categoryService.searchGrandTotalPage(searchCategory));

        session.setAttribute("parentCurrentPage", parentPage);
        session.setAttribute("childCurrentPage", childPage);
        session.setAttribute("grandCurrentPage", grandPage);

        logger.info("searchCategory method finished with response: {}", searchCategory, parentPage, childPage,
                grandPage);
        return "category-list";
    }

    /**
     * 親カテゴリのページング
     * 
     * @param parentPage 親カテゴリのcurrent page
     * @param model      requestスコープ
     * @return カテゴリ一覧へ
     */
    @RequestMapping("toParentSearch")
    public String toSearchParentPage(int parentPage, Model model) {
        logger.info("searchParentPage method started");

        // 検索条件がなければ、ページを更新して一覧表示へ
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

        logger.info("searchParentPage method finished with response: {}", parentPage);
        return "category-list";
    }

    /**
     * 子カテゴリのページング
     * 
     * @param childPage 子カテゴリのcurrent page
     * @param model     requestスコープ
     * @return カテゴリ一覧へ
     */
    @RequestMapping("toChildSearch")
    public String toSearchChildPage(int childPage, Model model) {
        logger.info("searchChildPage method started");

        // 検索条件がなければ、ページを更新して一覧表示へ
        String searchCondition = (String) session.getAttribute("categorySearchCondition");
        if (searchCondition == null) {
            int parentPage = (int) session.getAttribute("parentCurrentPage");
            int grandPage = (int) session.getAttribute("grandCurrentPage");

            logger.info("searchChildPage method finished with response: {}", childPage);
            return toCategoryList(parentPage, childPage, grandPage, model);
        }
        model.addAttribute("searchCondition", searchCondition);
        session.setAttribute("childCategoryList",
                categoryService.searchChildCategory(searchCondition, childPage));
        session.setAttribute("childTotalPage",
                categoryService.searchChildTotalPage(searchCondition));
        session.setAttribute("childCurrentPage", childPage);

        logger.info("searchChildPage method finished with response: {}", childPage);
        return "category-list";
    }

    /**
     * 孫カテゴリのページング
     * 
     * @param grandPage 孫カテゴリのcurrent page
     * @param model     requestスコープ
     * @return カテゴリ一覧へ
     */
    @RequestMapping("toGrandSearch")
    public String toSearchGrandPage(int grandPage, Model model) {
        logger.info("searchGrandPage method started");

        // 検索条件がなければ、ページを更新して一覧表示へ
        String searchCondition = (String) session.getAttribute("categorySearchCondition");
        if (searchCondition == null) {
            int parentPage = (int) session.getAttribute("parentCurrentPage");
            int childPage = (int) session.getAttribute("childCurrentPage");

            logger.info("searchGrandPage method finished with response: {}", grandPage);
            return toCategoryList(parentPage, childPage, grandPage, model);
        }

        model.addAttribute("searchCondition", searchCondition);
        session.setAttribute("grandCategoryList",
                categoryService.searchGrandCategory(searchCondition, grandPage));
        session.setAttribute("grandTotalPage",
                categoryService.searchGrandTotalPage(searchCondition));
        session.setAttribute("grandCurrentPage", grandPage);

        logger.info("searchGrandPage method finished with response: {}", grandPage);
        return "category-list";
    }

    /**
     * カテゴリ詳細画面
     * 
     * @param id    category id
     * @param model requestスコープ
     * @return 詳細画面へ
     */
    @RequestMapping("categoryDetail")
    public String categoryDetail(int id, Model model) {
        logger.info("categoryDetail method started");

        // idからcategory情報の取得
        model.addAttribute("category", categoryService.findById(id));
        // 対応する子カテゴリリストの取得
        model.addAttribute("childCategoryList", categoryService.findChildCategory(id));
        // 子カテゴリの件数の取得
        model.addAttribute("childCategoryCount", categoryService.childCategoryCount(id));

        logger.info("categoryDetail method finished with response: {}", id);
        return "category-detail";
    }

    /**
     * カテゴリの新規作成画面への遷移
     * 
     * @param categoryForm 入力情報の保存用
     * @param model        requestスコープ
     * @return 新規作成画面へ
     */
    @RequestMapping("toAddCategory")
    public String toAddCategory(CategoryForm categoryForm, Model model) {
        logger.info("addCategory method started");

        // エラーがあった場合はこれに入れて返す（初期は空）
        model.addAttribute("categoryForm", categoryForm);
        // 現在のカテゴリリストの取得
        model.addAttribute("categoryList", categoryService.findAllCategory());

        logger.info("categoryDetail method finished with response: {}", categoryForm);
        return "category-add";
    }

    /**
     * カテゴリの新規作成
     * 
     * @param categoryForm 入力情報の受け取り
     * @param model        requestスコープ
     * @return 成功ならconfirm画面へ、失敗なら新規作成画面へ
     */
    @PostMapping("addCategory")
    public String addCategory(CategoryForm categoryForm, Model model) {
        logger.info("addCategory method started");

        // すでに存在する組み合わせであればエラーとして戻る
        if (categoryService.checkCategory(categoryForm) != 0) {
            model.addAttribute("error", true);

            logger.warn("addCategory, checkCategory error");
            return toAddCategory(categoryForm, model);
        }

        // カテゴリの新規作成
        categoryService.insertCategory(categoryForm);

        logger.info("addCategory method finished with response: {}", categoryForm);
        return "confirm/add-category-confirm";
    }

    /**
     * カテゴリの編集画面への遷移
     * 
     * @param id    category id
     * @param model requestスコープ
     * @return 編集画面へ
     */
    @RequestMapping("toEditCategory")
    public String toEditCategory(int id, Model model) {
        // idからcategory情報を取得
        model.addAttribute("categoryData", categoryService.findById(id));

        return "category-edit";
    }

    /**
     * カテゴリの編集
     * 
     * @param form  入力情報の受け取り
     * @param model requestスコープ
     * @return 成功ならconfirm画面へ、失敗なら新規作成画面へ
     */
    @PostMapping("editCategory")
    public String editCategory(CategoryForm form, Model model) {
        logger.info("editCategory method started");

        // 入力がなければエラーで返す（
        if (form.getName() == null || form.getName() == "") {
            model.addAttribute("inputError", true);

            logger.warn("editCategory, input error");
            return toEditCategory(form.getId(), model);
        }
        // すでに存在する組み合わせであればエラーとして戻る
        if (categoryService.checkCategoryName(form.getName(), form.getParentId(), form.getNameAll()) != 0) {
            model.addAttribute("checkError", true);

            logger.warn("editCategory, checkCategoryName error");
            return toEditCategory(form.getId(), model);
        }
        // categoryのname,name_allを更新
        categoryService.editCategoryNameAndNameAll(form.getId(), form.getName(), form.getParentId(), form.getNameAll());

        // 画面遷移用のid受け渡し
        model.addAttribute("categoryId", form.getId());

        logger.info("editCategory method finished with response: {}", form);
        return "confirm/edit-category-confirm";
    }

    /**
     * カテゴリの削除
     * 
     * @param id       category id
     * @param parentId 親カテゴリのid
     * @param nameAll  フルパス
     * @param model    requestスコープ
     * @return 成功ならconfirm画面へ、失敗ならerror画面へ
     */
    @RequestMapping("deleteCategory")
    public String deleteCategory(int id, int parentId, String nameAll, Model model) {
        logger.info("deleteCategory method started");

        // 変更に関わるitemsの更新
        itemService.updateCategory(id, parentId, nameAll);
        // レコードの削除
        categoryService.delete(id);

        logger.info("deleteCategory method finished with response: {}", id, parentId, nameAll);
        return "confirm/delete-category-confirm";
    }

}
