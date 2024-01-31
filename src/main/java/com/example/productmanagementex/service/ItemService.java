package com.example.productmanagementex.service;

import java.util.List;

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
    private CategoryService categoryService;
    @Autowired
    private ItemRepository repository;

    /**
     * pageに対応する３０件を取得
     * 
     * @param page page
     * @return 検索結果
     */
    public List<Item> findAllItems(int page) {
        List<Item> itemList = repository.findAllItems(page);
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
            String grandCategory, int page) {
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder brandBuilder = new StringBuilder();
        StringBuilder nameAllBuilder = new StringBuilder();
        String nameAll = null;

        // nameの入力がなければ全件
        if (name == null) {
            name = "%";
        } else {
            nameBuilder.append("%");
            nameBuilder.append(name);
            nameBuilder.append("%");
            name = nameBuilder.toString();
        }

        // brandの入力がなければ全件
        if (brand == null) {
            brand = "%";
        } else {
            brandBuilder.append("%");
            brandBuilder.append(brand);
            brandBuilder.append("%");
            brand = brandBuilder.toString();
        }

        // categoryの選択条件で分岐
        if (parentCategory != "" && parentCategory != null) {
            if (childCategory != "" && childCategory != null) {
                if (grandCategory != "" && grandCategory != null) {
                    // 親、子、孫まで選択した時は 親/子/孫 で検索
                    nameAllBuilder.append(parentCategory);
                    nameAllBuilder.append("/");
                    nameAllBuilder.append(childCategory);
                    nameAllBuilder.append("/");
                    nameAllBuilder.append(grandCategory);
                    nameAll = nameAllBuilder.toString();
                } else {
                    // 孫を選択していない時は 親/子/% で検索
                    nameAllBuilder.append(parentCategory);
                    nameAllBuilder.append("/");
                    nameAllBuilder.append(childCategory);
                    nameAllBuilder.append("/%");
                    nameAll = nameAllBuilder.toString();
                }
            } else {
                // 親だけ選択の時は 親/% で検索
                nameAllBuilder.append(parentCategory);
                nameAllBuilder.append("/%");
                nameAll = nameAllBuilder.toString();
            }
        } else {
            // 選択がなかった場合は全件
            nameAll = "%";
        }

        List<Item> itemList = repository.searchItems(name, brand, nameAll, page);
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
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder brandBuilder = new StringBuilder();
        StringBuilder nameAllBuilder = new StringBuilder();
        String nameAll = null;

        if (name == "" || name == null) {
            name = "%";
        } else {
            nameBuilder.append("%");
            nameBuilder.append(name);
            nameBuilder.append("%");
            name = nameBuilder.toString();
        }

        if (brand == "" || brand == null) {
            brand = "%";
        } else {
            brandBuilder.append("%");
            brandBuilder.append(brand);
            brandBuilder.append("%");
            brand = brandBuilder.toString();
        }

        if (parentCategory != "" && parentCategory != null) {
            if (childCategory != "" && childCategory != null) {
                if (grandCategory != "" && grandCategory != null) {
                    nameAllBuilder.append(parentCategory);
                    nameAllBuilder.append("/");
                    nameAllBuilder.append(childCategory);
                    nameAllBuilder.append("/");
                    nameAllBuilder.append(grandCategory);
                    nameAll = nameAllBuilder.toString();
                } else {
                    nameAllBuilder.append(parentCategory);
                    nameAllBuilder.append("/");
                    nameAllBuilder.append(childCategory);
                    nameAllBuilder.append("/%");
                    nameAll = nameAllBuilder.toString();
                }
            } else {
                nameAllBuilder.append(parentCategory);
                nameAllBuilder.append("/%");
                nameAll = nameAllBuilder.toString();
            }
        } else {
            nameAll = "%";
        }

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
    public void addItem(ItemForm itemForm, CategoryForm categoryForm) {
        // categoryFormからname_all作成
        StringBuilder builder = new StringBuilder();
        builder.append(categoryForm.getParentCategory());
        builder.append("/");
        builder.append(categoryForm.getChildCategory());
        builder.append("/");
        builder.append(categoryForm.getGrandCategory());
        String nameAll = builder.toString();

        // domainクラスにコピー
        Item item = new Item();
        BeanUtils.copyProperties(itemForm, item);

        repository.insertItem(item, nameAll);
    }

    /**
     * 編集
     * 
     * @param itemForm     item情報
     * @param categoryForm category情報
     */
    public void editItem(ItemForm itemForm, CategoryForm categoryForm) {
        StringBuilder builder = new StringBuilder();
        builder.append(categoryForm.getParentCategory());
        builder.append("/");
        builder.append(categoryForm.getChildCategory());
        builder.append("/");
        builder.append(categoryForm.getGrandCategory());
        String nameAll = builder.toString();

        Item item = new Item();
        BeanUtils.copyProperties(itemForm, item);

        repository.updateItem(item, nameAll);
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
        // 検索結果があればtrueに、なければfalseに
        boolean check = false;
        Integer count = repository.checkDelete(id, updateTime);
        if (count != 0) {
            check = true;
        }
        return check;
    }

    /**
     * 論理削除
     * 
     * @param id id
     */
    public void delete(int id) {
        repository.changeDeleteStatus(id);
    }

    /**
     * categoryの更新
     * 
     * @param id       id
     * @param parentId parent_id
     * @param nameAll  name_all
     */
    public void updateCategory(int id, int parentId, String nameAll) {
        // 変更が必要なIDリストの取得
        List<Integer> changeRecordIdList = categoryService.findChangeRecordId(id, parentId, nameAll);
        repository.updateCategory(changeRecordIdList);
    }

}
