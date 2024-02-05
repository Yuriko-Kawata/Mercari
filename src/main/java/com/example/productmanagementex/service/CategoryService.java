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
    private CategoryRepository repository;

    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    public List<Category> findAllCategory() {
        List<Category> categoryList = repository.findAllCategory();
        return categoryList;
    }

    /**
     * カテゴリに重複するものがないかチェック
     * 
     * @param form form
     * @return 重複するものがあれば１、なければ０
     */
    public int checkCategory(CategoryForm form) {
        logger.debug("Starting checkCategory");
        // 子カテゴリの選択がなければ”カテゴリ無”を代入
        if (form.getChildCategory() == null || form.getChildCategory().equals("")) {
            form.setChildCategory("カテゴリ無");
        }
        // 孫カテゴリの選択がなければ”カテゴリ無”を代入
        if (form.getGrandCategory() == null || form.getGrandCategory().equals("")) {
            form.setGrandCategory("カテゴリ無");
        }

        // 親/子/孫でnameAllを作成
        StringBuilder builder = new StringBuilder();
        builder.append(form.getParentCategory());
        builder.append("/");
        builder.append(form.getChildCategory());
        builder.append("/");
        builder.append(form.getGrandCategory());
        String nameAll = builder.toString();

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
        StringBuilder builder = new StringBuilder();
        builder.append(form.getParentCategory());
        builder.append("/");
        builder.append(form.getChildCategory());
        builder.append("/");
        builder.append(form.getGrandCategory());
        String nameAll = builder.toString();

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
    public int totalParentPage() {
        int totalSize = repository.parentListSize();
        int totalpage = totalSize / 30 + 1;
        return totalpage;
    }

    /**
     * 子カテゴリのトータル件数取得
     * 
     * @return 検索結果
     */
    public int totalChildPage() {
        int totalSize = repository.childListSize();
        int totalpage = totalSize / 30 + 1;
        return totalpage;
    }

    /**
     * 孫カテゴリのトータル件数取得
     * 
     * @return 検索結果
     */
    public int totalGrandPage() {
        int totalSize = repository.grandListSize();
        int totalpage = totalSize / 30 + 1;
        return totalpage;
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
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String nameLike = builder.toString();

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

        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String nameLike = builder.toString();

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

        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String nameLike = builder.toString();

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
    public int searchParentTotalPage(String searchCondition) {
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String nameLike = builder.toString();

        int totalSize = repository.searchParentTotal(nameLike);
        int totalpage = totalSize / 30 + 1;
        return totalpage;
    }

    /**
     * 検索条件に一致する子カテゴリのトータル件数取得
     * 
     * @param searchCondition 検索条件
     * @return 検索結果
     */
    public int searchChildTotalPage(String searchCondition) {
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String nameLike = builder.toString();

        int totalSize = repository.searchChildTotal(nameLike);
        int totalpage = totalSize / 30 + 1;
        return totalpage;
    }

    /**
     * 検索条件に一致する孫カテゴリのトータル件数取得
     * 
     * @param searchCondition 検索条件
     * @return 検索結果
     */
    public int searchGrandTotalPage(String searchCondition) {
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String nameLike = builder.toString();

        int totalSize = repository.searchGrandTotal(nameLike);
        int totalpage = totalSize / 30 + 1;
        return totalpage;
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
     * カテゴリの更新に伴い、変更が必要なレコードのID取得
     * 
     * @param id       id
     * @param parentId parent_id
     * @param nameAll  name_all
     * @return 検索結果
     */
    public List<Integer> findChangeRecordId(int id, int parentId, String nameAll) {
        logger.debug("Started findChangeRecordId");

        // 親カテゴリならparentConditionが０、子なら１、孫なら２
        int parentCondition = 1;
        List<Integer> changeRecordId = new ArrayList<>();
        if (parentId != 0 && nameAll.equals("")) {
            parentCondition = 2;
        } else if (parentId != 0 && !(nameAll.equals(""))) {
            // 孫の場合はそのIDのみ渡す
            changeRecordId.add(id);

            logger.debug("Finished findChangeRecordId");
            return changeRecordId;
        }
        // 親、子の場合は変更が必要なレコードが複数のため、検索を行う
        changeRecordId = repository.findChangeRecordId(id, parentCondition);

        logger.debug("Finished findChangeRecordId");
        return changeRecordId;
    }

    /**
     * 削除
     * 
     * @param id id
     */
    public void delete(int id) {
        repository.delete(id);
    }

}
