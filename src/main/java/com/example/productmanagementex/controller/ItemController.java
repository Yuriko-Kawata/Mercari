package com.example.productmanagementex.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.example.productmanagementex.service.FileStorageService;
import com.example.productmanagementex.service.ImageService;
import com.example.productmanagementex.service.ItemService;
import com.example.productmanagementex.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * itemsのcontroller
 * 
 * @author hiraizumi
 */
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
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LogManager.getLogger(ItemController.class);

    /**
     * 商品一覧の表示
     * 
     * @param page  current page
     * @param model requestスコープ
     * @return 商品一覧へ
     */
    @RequestMapping("itemList")
    public String toItemList(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "i.id") String sort, @RequestParam(defaultValue = "ASC") String order,
            Model model) {
        // sessionに検索条件があれば削除
        if (session.getAttribute("form") != null) {
            session.removeAttribute("form");
        }

        // ログインしているユーザーの情報取得
        logger.info("getAuthentication method started");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserMail = authentication.getName();
        User user = userService.findUserByMail(currentUserMail);
        session.setAttribute("userName", user.getName());
        logger.info("getAuthentication method finished with response: {}", user);

        // defaultのソート情報の定義
        SearchForm form = new SearchForm();
        form.setSort(sort);
        form.setOrder(order);
        session.setAttribute("form", form);

        // 検索条件の作成（初期は””）
        model.addAttribute("searchCondition", form);
        // current pageから３０件取得
        model.addAttribute("itemList", itemService.findItems(sort, order, page));

        // トータル件数の取得と、ページ数の計算
        int totalItem = itemService.totalItem();
        int totalPage = totalPageCount(totalItem);
        // トータルページの格納
        model.addAttribute("totalPage", totalPage);
        // トータル件数の格納
        model.addAttribute("totalItemCount", totalItem);
        // current pageの更新
        model.addAttribute("currentPage", page);
        // categoryリストの取得
        model.addAttribute("categoryList", categoryService.findAllCategory());
        return "item-list";
    }

    /**
     * 検索
     * 
     * @param form  検索条件
     * @param model requestスコープ
     * @param page  current page
     * @return 商品一覧へ
     */
    @RequestMapping("search")
    public String toSearch(@Validated SearchForm form, BindingResult rs, Model model,
            @RequestParam(defaultValue = "1") int page) {
        logger.info("search method started call: {}", form);

        // defaultのソート情報の定義（serviceで条件分岐めんどくさくなるからここで）
        form.setSort("i.id");
        form.setOrder("ASC");

        // 検索条件のsessionスコープへの格納
        session.setAttribute("form", form);

        // 検索条件のrequeatスコープへの格納
        model.addAttribute("searchCondition", form);
        // 検索結果の取得
        model.addAttribute("itemList",
                itemService.searchItems(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory(), form.getSort(), form.getOrder(), page));

        // 検索件数の取得と、ページ数の計算
        int totalItem = itemService.searchTotalItem(form.getName(), form.getBrand(), form.getParentCategory(),
                form.getChildCategory(), form.getGrandCategory());
        if (totalItem == 0) {
            @SuppressWarnings("null") // 警告の抑制
            String errorMessage = messageSource.getMessage("error.0", null, Locale.getDefault());
            model.addAttribute("totalItemCountError", errorMessage);
        }
        int totalPage = totalPageCount(totalItem);

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalItemCount", totalItem);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllCategory());

        logger.info("search method finished with response: {}", totalItem);
        return "item-list";
    }

    @RequestMapping("sort")
    public String toSort(String sort, String order, Model model) {
        SearchForm form = (SearchForm) session.getAttribute("form");

        form.setSort(sort);
        form.setOrder(order);
        session.setAttribute("form", form);
        model.addAttribute("searchCondition", form);

        model.addAttribute("itemList",
                itemService.searchItems(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory(), form.getSort(), form.getOrder(), 1));

        int totalItem = itemService.searchTotalItem(form.getName(), form.getBrand(), form.getParentCategory(),
                form.getChildCategory(), form.getGrandCategory());
        int totalPage = totalPageCount(totalItem);

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalItemCount", totalItem);
        model.addAttribute("currentPage", 1);
        model.addAttribute("categoryList", categoryService.findAllCategory());

        logger.info("sort method finished with response: {}", totalItem);
        return "item-list";
    }

    /**
     * ページ遷移
     * 
     * @param page  current page
     * @param model requestスコープ
     * @return 商品一覧へ
     */
    @RequestMapping("toSearch")
    public String toSearchPage(int page, Model model) {
        logger.info("searchPage method started call: {}", page);

        // 検索条件がなければ、ページを更新して一覧表示へ
        SearchForm form = (SearchForm) session.getAttribute("form");
        if (form == null) {
            logger.info("searchPage method finished");
            String sort = "i.id";
            String order = "ASC";
            return toItemList(page, sort, order, model);
        }

        // 検索条件のpageを更新して取得
        model.addAttribute("searchCondition", form);
        model.addAttribute("itemList",
                itemService.searchItems(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory(), form.getSort(), form.getOrder(), page));
        int totalItem = itemService.searchTotalItem(form.getName(), form.getBrand(), form.getParentCategory(),
                form.getChildCategory(), form.getGrandCategory());
        int totalPage = totalPageCount(totalItem);

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalItemCount", totalItem);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryList", categoryService.findAllCategory());

        logger.info("searchPage method finished");
        return "item-list";
    }

    /**
     * categoryでのフィルター
     * 
     * @param name     カテゴリ名
     * @param parentId 親カテゴリのId
     * @param nameAll  フルパス
     * @param model    requestスコープ
     * @return 商品一覧へ
     */
    @RequestMapping("categoryFilter")
    public String categoryFilter(String name, int parentId, String nameAll, Model model) {
        logger.info("categoryFilter method started call: {}", name, parentId, nameAll);

        // 検索条件の更新
        // 選択したものが親カテゴリならparentCategoryのみ更新、子ならchildも、孫ならgrandも
        SearchForm form = new SearchForm();
        if (parentId == 0) {
            form.setParentCategory(name);
        } else if (nameAll.equals("")) {
            Category parentCategory = categoryService.findParentCategory(parentId);
            form.setParentCategory(parentCategory.getName());
            form.setChildCategory(name);
        } else {
            Category childCategory = categoryService.findParentCategory(parentId);
            Category parentCategory = categoryService.findParentCategory(childCategory.getParentId());
            form.setParentCategory(parentCategory.getName());
            form.setChildCategory(childCategory.getName());
            form.setGrandCategory(name);
        }
        session.setAttribute("form", form);

        // 更新した条件で検索し、取得
        model.addAttribute("searchCondition", form);
        model.addAttribute("itemList",
                itemService.searchItems(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory(), form.getSort(), form.getOrder(), 1));
        int totalItem = itemService.searchTotalItem(form.getName(), form.getBrand(), form.getParentCategory(),
                form.getChildCategory(), form.getGrandCategory());
        int totalPage = totalPageCount(totalItem);

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalItemCount", totalItem);
        model.addAttribute("currentPage", 1);
        model.addAttribute("categoryList", categoryService.findAllCategory());

        logger.info("categoryFilter method finished with response: {}", totalItem);
        return "item-list";
    }

    /**
     * brandでのフィルター
     * 
     * @param brand ブランド名
     * @param model requestスコープ
     * @return 商品一覧へ
     */
    @RequestMapping("brandFilter")
    public String brandFilter(String brand, Model model) {
        logger.info("brandFilter method started call: {}", brand);

        // 検索条件を取得したbrandに更新
        SearchForm form = new SearchForm();
        form.setBrand(brand);
        session.setAttribute("form", form);

        // 更新した条件で検索し、取得
        model.addAttribute("searchCondition", form);
        model.addAttribute("itemList",
                itemService.searchItems(form.getName(), form.getBrand(), form.getParentCategory(),
                        form.getChildCategory(), form.getGrandCategory(), form.getSort(), form.getOrder(), 1));
        int totalItem = itemService.searchTotalItem(form.getName(), form.getBrand(), form.getParentCategory(),
                form.getChildCategory(), form.getGrandCategory());
        int totalPage = totalPageCount(totalItem);

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalItemCount", totalItem);
        model.addAttribute("currentPage", 1);
        model.addAttribute("categoryList", categoryService.findAllCategory());

        logger.info("brandFilter method finished with response: {}", totalItem);
        return "item-list";
    }

    /**
     * 商品の新規作成画面への遷移
     * 
     * @param itemForm 入力情報の保存用
     * @param model    requestスコープ
     * @return 新規作成画面へ
     */
    @RequestMapping("toAdd")
    public String toAddItem(ItemForm itemForm, CategoryForm categoryForm, Model model) {
        // エラーがあった場合はこれに入れて返す（初期は空）
        model.addAttribute("itemForm", itemForm);
        model.addAttribute("categoryForm", categoryForm);
        // カテゴリリストの取得
        model.addAttribute("categoryList", categoryService.findAllCategory());

        return "add";
    }

    /**
     * 商品の新規作成
     * 
     * @param itemForm     item入力情報の受け取り
     * @param rs           入力チェック
     * @param categoryForm category入力情報の受け取り
     * @param model        requestスコープ
     * @return 成功ならconfirmへ、失敗なら新規作成画面にもどる
     */
    @PostMapping("add")
    public String addItem(@Validated ItemForm itemForm, BindingResult itemRs, @Validated CategoryForm categoryForm,
            BindingResult categoryRs, Model model) {
        logger.info("addItem method started call: {}", itemForm, categoryForm);

        // validationエラーがあれば元の画面に戻る
        if (itemRs.hasErrors()) {
            logger.warn("addItem, item validation error");
            return toAddItem(itemForm, categoryForm, model);
        }
        if (categoryRs.hasErrors()) {
            logger.warn("addItem, category validation error");
            return toAddItem(itemForm, categoryForm, model);
        }

        // itemsに新規追加と作成されたIDの取得
        int itemId = itemService.addItem(itemForm, categoryForm);

        // image pathを作成し、テーブルに保存
        String imagePath = fileStorageService.storeFile(itemForm.getImage());
        imageService.storage(itemId, imagePath);

        logger.info("addItem method finished");
        return "confirm/add-item-confirm";
    }

    /**
     * 詳細画面への遷移
     * 
     * @param id    item id
     * @param model requestスコープ
     * @return 詳細画面へ
     */
    @RequestMapping("detail")
    public String detail(int id, Model model) {
        // idからitem情報の取得
        model.addAttribute("item", itemService.findById(id));
        // idからimage pathの取得
        model.addAttribute("imagePath", imageService.getPath(id));
        return "detail";
    }

    /**
     * 商品の編集画面への遷移
     * 
     * @param id           item id
     * @param categoryForm 入力情報の受け取り
     * @param model        requestスコープ
     * @return 商品編集画面へ
     */
    @RequestMapping("toEdit")
    public String toEditItem(int id, ItemForm itemForm, CategoryForm categoryForm, Model model) {
        // itemsのupdate timeの取得
        model.addAttribute("updateTime", itemService.getUpdateTime(id));
        // image pathの取得
        model.addAttribute("imagePath", imageService.getPath(id));
        // エラーがあった場合はこれに入れて返す（初期は空）
        model.addAttribute("itemForm", itemForm);
        model.addAttribute("categoryForm", categoryForm);
        // カテゴリリストの取得
        model.addAttribute("categoryList", categoryService.findAllCategory());

        // item idから情報を取得
        Item item = itemService.findById(id);
        String originalParentCategory = null;
        String originalChildCategory = null;
        String originalGrandCategory = null;
        // itemのcategoryに対して、親、子、孫カテゴリのnameを取得
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
        // 現在の親カテゴリ
        model.addAttribute("originalParentCategory", originalParentCategory);
        // 現在の子カテゴリ
        model.addAttribute("originalChildCategory", originalChildCategory);
        // 現在の孫カテゴリ
        model.addAttribute("originalGrandCategory", originalGrandCategory);
        // item情報の格納
        model.addAttribute("itemData", item);

        return "edit";
    }

    /**
     * 商品編集
     * 
     * @param itemForm     入力情報の受け取り
     * @param categoryForm 入力情報の受け取り
     * @param model        requestスコープ
     * @return 成功ならconfirm画面へ、失敗なら編集画面に戻る
     */
    @PostMapping("edit")
    public String editItem(@Validated ItemForm itemForm, BindingResult rs, CategoryForm categoryForm, Model model) {
        logger.info("editItem method started call: {}", itemForm, categoryForm);

        if (rs.hasErrors()) {
            logger.warn("editItem, validation error");
            return toEditItem(itemForm.getId(), itemForm, categoryForm, model);
        }

        // categoryの入力がなければ、categoryは元情報のまま更新
        if (categoryForm.getParentCategory().equals("")) {
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
            // 元の情報をそのまま格納
            categoryForm.setParentCategory(originalParentCategory);
            categoryForm.setChildCategory(originalChildCategory);
            categoryForm.setGrandCategory(originalGrandCategory);

            // ファイルが送られた場合は、元画像の削除とpathの更新を行う
            if (!(itemForm.getImage().isEmpty())) {
                // 元画像の削除用にpathの取得
                String currentImagePath = imageService.getPath(itemForm.getId());
                // defaultの画像（no-image）でなければ削除
                if (!(currentImagePath.equals("uploaded-img/no-image.png"))) {
                    fileStorageService.deleteFile(currentImagePath);
                }

                // pathの更新
                String imagePath = fileStorageService.storeFile(itemForm.getImage());
                imageService.updatePath(itemForm.getId(), imagePath);
            }

            // item情報の更新
            itemService.editItem(itemForm, categoryForm);
            // 画面遷移用のid受け取り
            model.addAttribute("itemId", itemForm.getId());
            return "confirm/edit-item-confirm";

            // 親を選択したが、子、孫まで入力がなければエラーとして元の画面に戻る
        } else if (categoryForm.getChildCategory().equals("")) {
            @SuppressWarnings("null") // 警告の抑制
            String errorMessage = messageSource.getMessage("error.choiceCategory", null, Locale.getDefault());
            model.addAttribute("choiceError", errorMessage);

            logger.warn("editItem, category choice error");
            return toEditItem(itemForm.getId(), itemForm, categoryForm, model);
        } else if (categoryForm.getGrandCategory().equals("")) {
            @SuppressWarnings("null") // 警告の抑制
            String errorMessage = messageSource.getMessage("error.checkCategory", null, Locale.getDefault());
            model.addAttribute("choiceError", errorMessage);

            logger.warn("editItem, category choice error");
            return toEditItem(itemForm.getId(), itemForm, categoryForm, model);
        }

        if (itemForm.getImage() != null) {
            String imagePath = fileStorageService.storeFile(itemForm.getImage());
            imageService.updatePath(itemForm.getId(), imagePath);
        }

        itemService.editItem(itemForm, categoryForm);
        model.addAttribute("itemId", itemForm.getId());

        logger.info("editItem method finished");
        return "confirm/edit-item-confirm";
    }

    /**
     * 商品の削除
     * 
     * @param id         item id
     * @param updateTime item update_time
     * @param model      requestスコープ
     * @return 成功ならconfirm画面へ、失敗なら詳細画面に戻る
     */
    @RequestMapping("delete")
    public String deleteItem(int id, Timestamp updateTime, Model model) {
        logger.info("deleteItem method started call: {}", id, updateTime);

        // 排他制御を行い、update_timeが同じであれば削除を行う
        if (itemService.checkDelete(id, updateTime)) {
            itemService.delete(id);
        } else {
            logger.warn("deleteItem, checkDelete error");
            return "4xx";
        }

        logger.info("deleteItem method finished");
        return "confirm/delete-item-confirm";
    }

    /**
     * csvファイルでの商品データのダウンロード
     * 
     * @param response response
     * @throws IOException
     */
    @GetMapping("download")
    public void downloadItemData(HttpServletResponse response) throws IOException {
        List<Item> items;
        SearchForm form = (SearchForm) session.getAttribute("form");
        // 検索条件があればその条件のリストを、なければ全件取得
        if (form == null) {
            items = itemService.findAllItems(); // 商品データの取得
        } else {
            items = itemService.searchAllItems(form.getName(), form.getBrand(), form.getParentCategory(),
                    form.getChildCategory(), form.getGrandCategory());
        }

        // ファイル形式の指定
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"products.csv\"");

        // ヘッダーの書き込み
        PrintWriter writer = response.getWriter();
        writer.println("Product ID,Product Name,Price");

        // リストの書き込み
        for (Item item : items) {
            writer.println(item.getId() + "," + item.getName() + "," + item.getPrice());
        }

        writer.flush();
        writer.close();
    }

    /**
     * トータル件数からのトータルページ数の計算
     * 
     * @param totalItemCount トータル件数
     * @return トータルページ数
     */
    private int totalPageCount(int totalItemCount) {
        int totalPage = 0;
        // 表示の３０件で割り、ページ数を計算
        if (totalItemCount % 30 == 0) {
            totalPage = totalItemCount / 30;
            return totalPage;
        } else {
            totalPage = totalItemCount / 30 + 1;
            return totalPage;
        }
    }

}
