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
    @Autowired
    private MessageSource messageSource;

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

        // 親カテゴリのトータル件数の取得とページ数の計算
        int totalParentCategoryCount = categoryService.totalParentCount();
        int totalParentPage = totalPageCount(totalParentCategoryCount);
        session.setAttribute("totalParentCategoryCount", totalParentCategoryCount);
        session.setAttribute("totalParentCategoryPage", totalParentPage);
        // 子カテゴリのトータル件数の取得とページ数の計算
        int totalChildCategoryCount = categoryService.totalChildCount();
        int totalChildPage = totalPageCount(totalChildCategoryCount);
        session.setAttribute("totalChildCategoryCount", totalChildCategoryCount);
        session.setAttribute("totalChildCategoryPage", totalChildPage);
        // 孫カテゴリのトータル件数の取得とページ数の計算
        int totalGrandCategoryCount = categoryService.totalGrandCount();
        int totalGrandPage = totalPageCount(totalGrandCategoryCount);
        session.setAttribute("totalGrandCategoryCount", totalGrandCategoryCount);
        session.setAttribute("totalGrandCategoryPage", totalGrandPage);

        // 親カテゴリのcurrent pageの更新
        session.setAttribute("currentParentCategoryPage", parentPage);
        // 子カテゴリのcurrent pageの更新
        session.setAttribute("currentChildCategoryPage", childPage);
        // 孫カテゴリのcurrent pageの更新
        session.setAttribute("currentGrandCategoryPage", grandPage);

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
        logger.info("searchCategory method started call: {}", searchCategory, parentPage, childPage, grandPage);

        // 検索条件が””であれば、ページを更新して一覧表示へ
        if (searchCategory.equals("")) {

            logger.info("searchCategory method finished");
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

        int totalParentCategoryCount = categoryService.searchTotalParentCount(searchCategory);
        int totalParentPage = totalPageCount(totalParentCategoryCount);
        session.setAttribute("totalParentCategoryCount", totalParentCategoryCount);
        session.setAttribute("totalParentCategoryPage", totalParentPage);

        int totalChildCategoryCount = categoryService.searchTotalChildCount(searchCategory);
        int totalChildPage = totalPageCount(totalChildCategoryCount);
        session.setAttribute("totalChildCategoryCount", totalChildCategoryCount);
        session.setAttribute("totalChildCategoryPage", totalChildPage);

        int totalGrandCategoryCount = categoryService.searchTotalGrandCount(searchCategory);
        int totalGrandPage = totalPageCount(totalGrandCategoryCount);
        session.setAttribute("totalGrandCategoryCount", totalGrandCategoryCount);
        session.setAttribute("totalGrandCategoryPage", totalGrandPage);

        session.setAttribute("currentParentCategoryPage", parentPage);
        session.setAttribute("currentChildCategoryPage", childPage);
        session.setAttribute("currentGrandCategoryPage", grandPage);

        logger.info("searchCategory method finished");
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
        logger.info("searchParentPage method startedcall: {}", parentPage);

        // 検索条件がなければ、ページを更新して一覧表示へ
        String searchCondition = (String) session.getAttribute("categorySearchCondition");
        if (searchCondition == null) {
            int childPage = (int) session.getAttribute("currentChildCategoryPage");
            int grandPage = (int) session.getAttribute("currentGrandCategoryPage");

            logger.info("searchParentPage method finished");
            return toCategoryList(parentPage, childPage, grandPage, model);
        }
        model.addAttribute("searchCondition", searchCondition);
        session.setAttribute("parentCategoryList",
                categoryService.searchParentCategory(searchCondition, parentPage));

        int totalParentCategoryCount = categoryService.searchTotalParentCount(searchCondition);
        int totalParentPage = totalPageCount(totalParentCategoryCount);
        session.setAttribute("totalParentCategoryCount", totalParentCategoryCount);
        session.setAttribute("totalParentCategoryPage", totalParentPage);
        session.setAttribute("currentParentCategoryPage", parentPage);

        logger.info("searchParentPage method finished");
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
        logger.info("searchChildPage method started call: {}", childPage);

        // 検索条件がなければ、ページを更新して一覧表示へ
        String searchCondition = (String) session.getAttribute("categorySearchCondition");
        if (searchCondition == null) {
            int parentPage = (int) session.getAttribute("currentParentCategoryPage");
            int grandPage = (int) session.getAttribute("currentGrandCategoryPage");

            logger.info("searchChildPage method finished");
            return toCategoryList(parentPage, childPage, grandPage, model);
        }
        model.addAttribute("searchCondition", searchCondition);
        session.setAttribute("childCategoryList",
                categoryService.searchChildCategory(searchCondition, childPage));
        int totalChildCategoryCount = categoryService.searchTotalChildCount(searchCondition);
        int totalChildPage = totalPageCount(totalChildCategoryCount);
        session.setAttribute("totalChildCategoryCount", totalChildCategoryCount);
        session.setAttribute("totalChildCategoryPage", totalChildPage);
        session.setAttribute("currentChildCategoryPage", childPage);

        logger.info("searchChildPage method finished");
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
        logger.info("searchGrandPage method started call: {}", grandPage);

        // 検索条件がなければ、ページを更新して一覧表示へ
        String searchCondition = (String) session.getAttribute("categorySearchCondition");
        if (searchCondition == null) {
            int parentPage = (int) session.getAttribute("currentParentCategoryPage");
            int childPage = (int) session.getAttribute("currentChildCategoryPage");

            logger.info("searchGrandPage method finished");
            return toCategoryList(parentPage, childPage, grandPage, model);
        }

        model.addAttribute("searchCondition", searchCondition);
        session.setAttribute("grandCategoryList",
                categoryService.searchGrandCategory(searchCondition, grandPage));
        int totalGrandCategoryCount = categoryService.searchTotalGrandCount(searchCondition);
        int totalGrandPage = totalPageCount(totalGrandCategoryCount);
        session.setAttribute("totalGrandCategoryCount", totalGrandCategoryCount);
        session.setAttribute("totalGrandCategoryPage", totalGrandPage);
        session.setAttribute("currentGrandCategoryPage", grandPage);

        logger.info("searchGrandPage method finished");
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
        logger.info("categoryDetail method started call: {}", id);

        // idからcategory情報の取得
        model.addAttribute("category", categoryService.findById(id));
        // 対応する子カテゴリリストの取得
        model.addAttribute("childCategoryList", categoryService.findChildCategory(id));
        // 子カテゴリの件数の取得
        int childCategoryCount = categoryService.childCategoryCount(id);
        if (childCategoryCount == 0) {
            model.addAttribute("itemCount", itemService.countItemByCategory(id));
        }
        model.addAttribute("childCategoryCount", childCategoryCount);

        logger.info("categoryDetail method finished");
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
        logger.info("addCategory method started call: {}", categoryForm);

        // エラーがあった場合はこれに入れて返す（初期は空）
        model.addAttribute("categoryForm", categoryForm);
        // 現在のカテゴリリストの取得
        model.addAttribute("categoryList", categoryService.findAllCategory());

        logger.info("categoryDetail method finished");
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
    public String addCategory(@Validated CategoryForm categoryForm, BindingResult rs, Model model) {
        logger.info("addCategory method started call: {}", categoryForm);

        if (rs.hasErrors()) {
            return toAddCategory(categoryForm, model);
        }

        // すでに存在する組み合わせであればエラーとして戻る
        if (categoryService.checkCategory(categoryForm) != 0) {
            @SuppressWarnings("null") // 警告の抑制
            String errorMessage = messageSource.getMessage("error.check.existCategory", null, Locale.getDefault());
            model.addAttribute("error", errorMessage);

            logger.warn("addCategory, check.existCategory error");
            return toAddCategory(categoryForm, model);
        }

        // カテゴリの新規作成
        categoryService.insertCategory(categoryForm);

        logger.info("addCategory method finished");
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
        logger.info("editCategory method started call: {}", form);

        // 入力がなければエラーで返す（
        if (form.getName().equals("")) {
            // ここだけエラーメッセージ直書き（validated使えないため）
            model.addAttribute("inputError", "nameを入力してください");
            logger.warn("editCategory, input error");
            return toEditCategory(form.getId(), model);
        }
        // すでに存在する組み合わせであればエラーとして戻る
        if (categoryService.checkCategoryName(form.getName(), form.getParentId(), form.getNameAll()) != 0) {
            @SuppressWarnings("null") // 警告の抑制
            String errorMessage = messageSource.getMessage("error.check.existCategory", null, Locale.getDefault());
            model.addAttribute("checkError", errorMessage);

            logger.warn("editCategory, checkCategoryName error");
            return toEditCategory(form.getId(), model);
        }
        // categoryのname,name_allを更新
        categoryService.editCategoryNameAndNameAll(form.getId(), form.getName(), form.getParentId(), form.getNameAll());

        // 画面遷移用のid受け渡し
        model.addAttribute("categoryId", form.getId());

        logger.info("editCategory method finished");
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
        logger.info("deleteCategory method started call: {}", id, parentId, nameAll);

        if (!(categoryService.checkDeleteCategory(id, parentId, nameAll))) {
            @SuppressWarnings("null") // 警告の抑制
            String errorMessage = messageSource.getMessage("error.checkDeleteCategory", null, Locale.getDefault());
            model.addAttribute("deleteError", errorMessage);
            return toEditCategory(id, model);
        }

        // レコードの削除
        categoryService.delete(id);

        logger.info("deleteCategory method finished");
        return "confirm/delete-category-confirm";
    }

    /**
     * トータル件数からのトータルページ数の計算
     * 
     * @param totalCategoryCount トータル件数
     * @return トータルページ数
     */
    private int totalPageCount(int totalCategoryCount) {
        int totalPage = 0;
        // 表示の３０件で割り、ページ数を計算
        if (totalCategoryCount % 30 == 0) {
            totalPage = totalCategoryCount / 30;
            return totalPage;
        } else {
            totalPage = totalCategoryCount / 30 + 1;
            return totalPage;
        }
    }

}
