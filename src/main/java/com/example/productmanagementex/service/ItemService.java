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

@Transactional
@Service
public class ItemService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemRepository repository;

    public List<Item> findAllItems(int page) {
        List<Item> itemList = repository.findAllItems(page);
        return itemList;
    }

    public int totalItem() {
        int itemListSize = repository.itemListSize();
        return itemListSize;
    }

    public List<Item> searchItems(String name, String brand, String parentCategory, String childCategory,
            String grandCategory, int page) {
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder brandBuilder = new StringBuilder();
        StringBuilder nameAllBuilder = new StringBuilder();
        String nameAll = null;

        if (name == null) {
            name = "%";
        } else {
            nameBuilder.append("%");
            nameBuilder.append(name);
            nameBuilder.append("%");
            name = nameBuilder.toString();
        }

        if (brand == null) {
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

        List<Item> itemList = repository.searchItems(name, brand, nameAll, page);
        return itemList;
    }

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

    public Item findById(int id) {
        return repository.findById(id);
    }

    public void addItem(ItemForm itemForm, CategoryForm categoryForm) {
        StringBuilder builder = new StringBuilder();
        builder.append(categoryForm.getParentCategory());
        builder.append("/");
        builder.append(categoryForm.getChildCategory());
        builder.append("/");
        builder.append(categoryForm.getGrandCategory());
        String nameAll = builder.toString();

        Item item = new Item();
        BeanUtils.copyProperties(itemForm, item);

        repository.insertItem(item, nameAll);
    }

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

    public Timestamp getUpdateTime(int id) {
        Timestamp updateTime = repository.getUpdateTime(id);
        System.out.println(updateTime);
        return updateTime;
    }

    public boolean checkDelete(int id, Timestamp updateTime) {
        boolean check = false;
        Integer count = repository.checkDelete(id, updateTime);
        if (count != 0) {
            check = true;
        }
        return check;
    }

    public void delete(int id) {
        repository.changeDeleteStatus(id);
    }

    public void updateCategory(int id, int parentId, String nameAll) {
        List<Integer> changeRecordIdList = categoryService.findChangeRecordId(id, parentId, nameAll);
        repository.updateCategory(changeRecordIdList);
    }

}
