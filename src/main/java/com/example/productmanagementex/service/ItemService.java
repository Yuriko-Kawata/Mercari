package com.example.productmanagementex.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.Item;
import com.example.productmanagementex.form.CategoryForm;
import com.example.productmanagementex.form.ItemForm;
import com.example.productmanagementex.repository.ItemRepository;

import java.sql.Timestamp;

/**
 * itemのserviceクラス
 * 
 * @author hiraizumi
 */
@Transactional
@Service
public class ItemService {

    @Autowired
    private ItemRepository repository;

    private static final Logger logger = LogManager.getLogger(ItemService.class);

    /**
     * 全件取得
     * 
     * @return 全件
     */
    public List<Item> findAllItems() {
        List<Item> items = repository.findAllItems();
        return items;
    }

    /**
     * pageに対応する３０件を取得
     * 
     * @param page page
     * @return 検索結果
     */
    public List<Item> findItems(String sort, String order, int page) {
        List<Item> itemList = repository.findItems(sort, order, page);
        return itemList;
    }

    /**
     * トータル件数の取得
     * 
     * @return 検索結果
     */
    public int totalItem() {
        int itemListSize = repository.itemListSize();
        return itemListSize;
    }

    /**
     * 検索条件に一致するレコードの全件取得
     * 
     * @param name           name
     * @param brand          brand
     * @param parentCategory 親カテゴリ
     * @param childCategory  子カテゴリ
     * @param grandCategory  孫カテゴリ
     * @return 検索結果
     */
    public List<Item> searchAllItems(String name, String brand, String parentCategory, String childCategory,
            String grandCategory) {
        logger.debug("Started searchAllItems");

        // nameの入力がなければ全件
        if (name == null) {
            name = "%";
        } else {
            name = ambigiousSearch(name);
        }

        // brandの入力がなければ全件
        if (brand == null) {
            brand = "%";
        } else {
            brand = ambigiousSearch(brand);
        }

        // categoryの選択条件で分岐
        String nameAll = makeNameAll(parentCategory, childCategory, grandCategory);

        List<Item> itemList = repository.searchAllItems(name, brand, nameAll);

        logger.debug("Finished searchAllItems");
        return itemList;
    }

    /**
     * 検索条件に一致するレコードの検索（pageに対応する３０件）
     * 
     * @param name           name
     * @param brand          brand
     * @param parentCategory 親カテゴリ
     * @param childCategory  子カテゴリ
     * @param grandCategory  孫カテゴリ
     * @param page           page
     * @return 検索結果
     */
    public List<Item> searchItems(String name, String brand, String parentCategory, String childCategory,
            String grandCategory, String sort, String order, int page) {
        logger.debug("Started searchItems");

        // nameの入力がなければ全件
        if (name == null) {
            name = "%";
        } else {
            name = ambigiousSearch(name);
        }

        // brandの入力がなければ全件
        if (brand == null) {
            brand = "%";
        } else {
            brand = ambigiousSearch(brand);
        }

        // categoryの選択条件で分岐
        String nameAll = makeNameAll(parentCategory, childCategory, grandCategory);

        List<Item> itemList = repository.searchItems(name, brand, nameAll, sort, order, page);

        logger.debug("Finished searchItems");
        return itemList;
    }

    /**
     * 検索条件に一致するトータル件数の取得
     * 
     * @param name           name
     * @param brand          brand
     * @param parentCategory 親カテゴリ
     * @param childCategory  子カテゴリ
     * @param grandCategory  孫カテゴリ
     * @return 検索結果
     */
    public int searchTotalItem(String name, String brand, String parentCategory, String childCategory,
            String grandCategory) {

        if (name == "" || name == null) {
            name = "%";
        } else {
            name = ambigiousSearch(name);
        }

        if (brand == "" || brand == null) {
            brand = "%";
        } else {
            brand = ambigiousSearch(brand);
        }

        String nameAll = makeNameAll(parentCategory, childCategory, grandCategory);

        int itemListSize = repository.searchItemsSize(name, brand, nameAll);
        return itemListSize;
    }

    /**
     * idで検索
     * 
     * @param id id
     * @return 検索結果
     */
    public Item findById(int id) {
        return repository.findById(id);
    }

    /**
     * 新規作成
     * 
     * @param itemForm     item情報
     * @param categoryForm category情報
     */
    @SuppressWarnings("null") // 警告の抑制
    public int addItem(ItemForm itemForm, CategoryForm categoryForm) {
        logger.debug("Started addItem");

        // categoryFormからname_all作成
        String nameAll = makeFullNameAll(categoryForm.getParentCategory(), categoryForm.getChildCategory(),
                categoryForm.getGrandCategory());

        // domainクラスにコピー
        Item item = new Item();
        BeanUtils.copyProperties(itemForm, item);

        int itemId = repository.insertItem(item, nameAll);
        logger.debug("Finished addItem");
        return itemId;
    }

    /**
     * 編集
     * 
     * @param itemForm     item情報
     * @param categoryForm category情報
     */
    @SuppressWarnings("null") // 警告の抑制
    public void editItem(ItemForm itemForm, CategoryForm categoryForm) {
        logger.debug("Started editItem");

        String nameAll = makeFullNameAll(categoryForm.getParentCategory(), categoryForm.getChildCategory(),
                categoryForm.getGrandCategory());

        Item item = new Item();
        BeanUtils.copyProperties(itemForm, item);

        repository.updateItem(item, nameAll);
        logger.debug("Finished editItem");
    }

    /**
     * idで指定したupdate_timeの取得
     * 
     * @param id id
     * @return update_time
     */
    public Timestamp getUpdateTime(int id) {
        Timestamp updateTime = repository.getUpdateTime(id);
        return updateTime;
    }

    /**
     * idで指定したレコードに対して、update_timeとdel_flgをチェック
     * 
     * @param id         id
     * @param updateTime update_time
     * @return 検索条件に一致するものがあればtrue、なければfalse
     */
    public boolean checkDelete(int id, Timestamp updateTime) {
        logger.debug("Started checkDelete");

        // 検索結果があればtrueに、なければfalseに
        boolean check = false;
        Integer count = repository.checkDelete(id, updateTime);
        if (count != 0) {
            check = true;
        }

        logger.debug("Finished checkDelete");
        return check;
    }

    /**
     * 論理削除
     * 
     * @param id id
     */
    public void delete(int id) {
        logger.debug("Started delete");
        repository.changeDeleteStatus(id);
        logger.debug("Finished delete");
    }

    /**
     * categoryに一致する商品の検索
     * 
     * @param id id
     * @return 検索結果
     */
    public Integer countItemByCategory(int id) {
        Integer itemCount = repository.countItemByCategory(id);
        return itemCount;
    }

    /**
     * name_allを作成（親、子、孫全てNULLでない時）
     * 
     * @param parentCategory 親カテゴリ名
     * @param childCategory  子カテゴリ名
     * @param grandCategory  孫カテゴリ名
     * @return name_all
     */
    private String makeFullNameAll(String parentCategory, String childCategory, String grandCategory) {
        // 曖昧検索用の文字列作成（parent/child/grand）
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
     * name_allの作成（全条件）
     * 
     * @param parentCategory 親カテゴリ名
     * @param childCategory  子カテゴリ名
     * @param grandCategory  孫カテゴリ名
     * @return name_all
     */
    private String makeNameAll(String parentCategory, String childCategory, String grandCategory) {
        StringBuilder nameAllBuilder = new StringBuilder();
        String nameAll;
        if (parentCategory != "" && parentCategory != null) {
            if (childCategory != "" && childCategory != null) {
                if (grandCategory != "" && grandCategory != null) {
                    // 親、子、孫まであれば 親/子/孫
                    nameAllBuilder.append(parentCategory);
                    nameAllBuilder.append("/");
                    nameAllBuilder.append(childCategory);
                    nameAllBuilder.append("/");
                    nameAllBuilder.append(grandCategory);
                    nameAll = nameAllBuilder.toString();
                } else {
                    // 親、子まであれば 親/子/%
                    nameAllBuilder.append(parentCategory);
                    nameAllBuilder.append("/");
                    nameAllBuilder.append(childCategory);
                    nameAllBuilder.append("/%");
                    nameAll = nameAllBuilder.toString();
                }
            } else {
                // 親まであれば 親/%
                nameAllBuilder.append(parentCategory);
                nameAllBuilder.append("/%");
                nameAll = nameAllBuilder.toString();
            }
        } else {
            // なければ全件
            nameAll = "%";
        }
        return nameAll;
    }

    /**
     * 曖昧検索用の文字列作成
     * 
     * @param searchCondition 検索条件
     * @return 曖昧検索用の文字列
     */
    private String ambigiousSearch(String searchCondition) {
        // 曖昧検索用の文字列作成（%condition%）
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String nameLike = builder.toString();
        return nameLike;
    }

}
