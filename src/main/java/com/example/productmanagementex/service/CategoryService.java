package com.example.productmanagementex.service;

import java.util.List;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.Category;
import com.example.productmanagementex.form.CategoryForm;
import com.example.productmanagementex.repository.CategoryRepository;

/**
 * categoryのserviceクラス
 * 
 * @author hiraizumi
 */
@Transactional
@Service
public class CategoryService {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryRepository repository;

    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    /**
     * 全件検索
     * 
     * @return 検索結果
     */
    public List<Category> findAllCategory() {
        List<Category> categoryList = repository.findAllCategory();
        return categoryList;
    }

    /**
     * idで検索
     * 
     * @param id id
     * @return 検索結果
     */
    public Category findById(int id) {
        Category category = repository.findById(id);
        return category;
    }

    /**
     * カテゴリに重複するものがないかチェック
     * 
     * @param form form
     * @return 重複するものがあれば１、なければ０
     */
    public int checkCategory(CategoryForm form) {
        logger.debug("Starting checkCategory");

        // 親/子/孫でnameAllを作成
        String nameAll = makeNameAll(form.getParentCategory(), form.getChildCategory(), form.getGrandCategory());
        // nameAllに一致するものがあるか確認
        int count = repository.checkCategory(nameAll);

        logger.debug("Finished checkCategory");
        return count;
    }

    /**
     * name, parent_idの組み合わせで一致するものがあるかチェック
     * 
     * @param name     name
     * @param parentId parent_id
     * @param nameAll  name_all
     * @return 検索条件に合うものがあれば１、なければ０
     */
    public int checkCategoryName(String name, int parentId, String nameAll) {
        logger.debug("Starting checkCategoryName");

        // 親カテゴリならparentConditionが０、子なら１、孫なら２
        int parentCondition = 0;
        if (parentId != 0) {
            if (nameAll.equals("")) {
                parentCondition = 1;
            } else {
                parentCondition = 2;
            }
        }

        // nameとparentCondition(階層)で一致するものがないか確認
        int count = repository.checkCategoryName(name, parentCondition);

        logger.debug("Finished checkCategoryName");
        return count;
    }

    /**
     * カテゴリの新規作成
     * 
     * @param form form
     */
    public void insertCategory(CategoryForm form) {
        logger.debug("Started insertCategory");

        // 親/子/孫でnameAllを作成
        String nameAll = makeNameAll(form.getParentCategory(), form.getChildCategory(), form.getGrandCategory());

        // categoryテーブルに新規作成
        repository.insertCategory(nameAll);
        logger.debug("Finished insertCategory");
    }

    /**
     * nameとname_allの更新
     * 
     * @param id       id
     * @param name     name
     * @param parentId parent_id
     * @param nameAll  name_all
     */
    public void editCategoryNameAndNameAll(int id, String name, int parentId, String nameAll) {
        logger.debug("Started editCategoryNameAndNameAll");

        // 階層
        int parentCondition = 1;
        // idで検索した元のname Menとか
        String originalName = repository.findNameById(id);
        // name_allの変更したい部分に入れる文字列 /Women/とか
        String insertName = null;
        // name_allの変更したい元の部分 /Men/とか
        String insertOriginalName = null;
        // name_all検索用の文字列 %/Men/%とか
        String originalNameLike = null;
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder originalNameBuilder = new StringBuilder();

        // 親、子、孫で動的に変化
        if (parentId != 0) {
            if (nameAll.equals("")) {
                // 子の時
                parentCondition = 2;
                nameBuilder.append("/");
                nameBuilder.append(name);
                nameBuilder.append("/");
                insertName = nameBuilder.toString();

                originalNameBuilder.append("/");
                originalNameBuilder.append(originalName);
                originalNameBuilder.append("/");
                insertOriginalName = originalNameBuilder.toString();
                originalNameBuilder.insert(0, "%");
                originalNameBuilder.append("%");
                originalNameLike = originalNameBuilder.toString();
            } else {
                // 孫の時
                parentCondition = 3;
                nameBuilder.append("/");
                nameBuilder.append(name);
                insertName = nameBuilder.toString();

                originalNameBuilder.append("/");
                originalNameBuilder.append(originalName);
                insertOriginalName = originalNameBuilder.toString();
                originalNameBuilder.insert(0, "%");
                originalNameLike = originalNameBuilder.toString();
            }
        } else {
            // 親の時
            nameBuilder.append(name);
            nameBuilder.append("/");
            insertName = nameBuilder.toString();

            originalNameBuilder.append(originalName);
            originalNameBuilder.append("/");
            insertOriginalName = originalNameBuilder.toString();
            originalNameBuilder.append("%");
            originalNameLike = originalNameBuilder.toString();
        }

        // nameの更新
        repository.editCategoryName(id, name, parentCondition);
        // name_allの更新
        repository.editCategoryNameAll(id, insertName, insertOriginalName, originalNameLike);
        logger.debug("Finished editCategoryNameAndNameAll");
    }

    /**
     * pageに対応した３０件の親カテゴリ取得
     * 
     * @param page page
     * @return 検索結果
     */
    public List<Category> findAllParentCategory(int page) {
        List<Category> categoryList = repository.findAllParentCategory(page);
        return categoryList;
    }

    /**
     * pageに対応した３０件の子カテゴリ取得
     * 
     * @param page page
     * @return 検索結果
     */
    public List<Category> findAllChildCategory(int page) {
        List<Category> categoryList = repository.findAllChildCategory(page);
        return categoryList;
    }

    /**
     * pageに対応した３０件の孫カテゴリ取得
     * 
     * @param page page
     * @return 検索結果
     */
    public List<Category> findAllGrandCategory(int page) {
        List<Category> categoryList = repository.findAllGrandCategory(page);
        return categoryList;
    }

    /**
     * 親カテゴリのトータル件数取得
     * 
     * @return 検索結果
     */
    public int totalParentCount() {
        int totalSize = repository.parentListSize();
        return totalSize;
    }

    /**
     * 子カテゴリのトータル件数取得
     * 
     * @return 検索結果
     */
    public int totalChildCount() {
        int totalSize = repository.childListSize();
        return totalSize;
    }

    /**
     * 孫カテゴリのトータル件数取得
     * 
     * @return 検索結果
     */
    public int totalGrandCount() {
        int totalSize = repository.grandListSize();
        return totalSize;
    }

    /**
     * 検索結果に合う親カテゴリの取得（pageに対応する３０件）
     * 
     * @param searchCondition 検索条件
     * @param page            page
     * @return 検索結果
     */
    public List<Category> searchParentCategory(String searchCondition, int page) {
        logger.debug("Started searchParentCategory");

        // 曖昧検索用の文字列作成
        String nameLike = ambigiousSearch(searchCondition);

        // 作成した文字列を用いて検索
        List<Category> categoryList = repository.searchParentCategory(nameLike, page);

        logger.debug("Finished searchParentCategory");
        return categoryList;
    }

    /**
     * 検索結果に合う子カテゴリの取得（pageに対応する３０件）
     * 
     * @param searchCondition 検索条件
     * @param page            page
     * @return 検索結果
     */
    public List<Category> searchChildCategory(String searchCondition, int page) {
        logger.debug("Started searchChildCategory");

        String nameLike = ambigiousSearch(searchCondition);

        List<Category> categoryList = repository.searchChildCategory(nameLike, page);

        logger.debug("Finished searchChildCategory");
        return categoryList;
    }

    /**
     * 検索結果に合う孫カテゴリの取得（pageに対応する３０件）
     * 
     * @param searchCondition 検索条件
     * @param page            page
     * @return 検索結果
     */
    public List<Category> searchGrandCategory(String searchCondition, int page) {
        logger.debug("Started searchGrandCategory");

        String nameLike = ambigiousSearch(searchCondition);

        List<Category> categoryList = repository.searchGrandCategory(nameLike, page);

        logger.debug("Finished searchGrandCategory");
        return categoryList;
    }

    /**
     * 検索条件に一致する親カテゴリのトータル件数取得
     * 
     * @param searchCondition 検索条件
     * @return 検索結果
     */
    public int searchTotalParentCount(String searchCondition) {
        String nameLike = ambigiousSearch(searchCondition);

        int totalSize = repository.searchParentTotal(nameLike);
        return totalSize;
    }

    /**
     * 検索条件に一致する子カテゴリのトータル件数取得
     * 
     * @param searchCondition 検索条件
     * @return 検索結果
     */
    public int searchTotalChildCount(String searchCondition) {
        String nameLike = ambigiousSearch(searchCondition);

        int totalSize = repository.searchChildTotal(nameLike);
        return totalSize;
    }

    /**
     * 検索条件に一致する孫カテゴリのトータル件数取得
     * 
     * @param searchCondition 検索条件
     * @return 検索結果
     */
    public int searchTotalGrandCount(String searchCondition) {
        String nameLike = ambigiousSearch(searchCondition);

        int totalSize = repository.searchGrandTotal(nameLike);
        return totalSize;
    }

    /**
     * idに対応するカテゴリの子カテゴリ数を取得
     * 
     * @param id id
     * @return 検索結果
     */
    public int childCategoryCount(int id) {
        int count = repository.childCategorySize(id);
        return count;
    }

    /**
     * parent_idから親カテゴリを取得
     * 
     * @param parentId parent_id
     * @return 検索結果
     */
    public Category findParentCategory(int parentId) {
        Category category = repository.findParentCategory(parentId);
        return category;
    }

    /**
     * idに対応するカテゴリの子カテゴリを取得
     * 
     * @param id id
     * @return 検索結果
     */
    public List<Category> findChildCategory(int id) {
        List<Category> categoryList = repository.findChildCategory(id);
        return categoryList;
    }

    /**
     * 対応する子カテゴリ、商品がないか確認
     * 
     * @param id       id
     * @param parentId parent_id
     * @param nameAll  name_all
     * @return 対応するものがあればfalse、なければtrue
     */
    public boolean checkDeleteCategory(int id, int parentId, String nameAll) {
        List<Category> categoryList = new ArrayList<>();
        int itemCount = 0;
        if (nameAll.equals("")) {
            categoryList = repository.findChildCategory(id);
        } else {
            itemCount = itemService.countItemByCategory(id);
        }

        if (categoryList.size() != 0 || itemCount != 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 削除
     * 
     * @param id id
     */
    public void delete(int id) {
        repository.delete(id);
    }

    /**
     * name_allを作成
     * 
     * @param parentCategory 親カテゴリ名
     * @param childCategory  子カテゴリ名
     * @param grandCategory  孫カテゴリ名
     * @return name_all
     */
    private String makeNameAll(String parentCategory, String childCategory, String grandCategory) {
        StringBuilder builder = new StringBuilder();
        builder.append(parentCategory);
        builder.append("/");
        builder.append(childCategory);
        builder.append("/");
        builder.append(grandCategory);
        String nameAll = builder.toString();
        return nameAll;
    }

    /**
     * 曖昧検索用の文字列作成
     * 
     * @param searchCondition 検索条件
     * @return 曖昧検索用の文字列
     */
    private String ambigiousSearch(String searchCondition) {
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String nameLike = builder.toString();
        return nameLike;
    }
}
