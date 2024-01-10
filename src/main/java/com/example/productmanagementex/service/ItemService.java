package com.example.productmanagementex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.Item;
import com.example.productmanagementex.repository.ItemRepository;

@Transactional
@Service
public class ItemService {

    @Autowired
    private ItemRepository repository;

    public List<Item> findAllItems(int page) {
        List<Item> itemList = repository.findAllItems(page);
        return itemList;
    }

    public int itemListSize() {
        return repository.itemListSize();
    }

    public List<Item> searchItems(String name, String brand, String parentCategory, String childCategory,
            String grandCategory, int page) {
        List<Item> itemList = repository.searchItems(name, brand, parentCategory, childCategory, grandCategory, page);
        return itemList;
    }

    public int searchItemsSize(String name, String brand, String parentCategory, String childCategory,
            String grandCategory) {
        int itemListSize = repository.searchItemsSize(name, brand, parentCategory, childCategory, grandCategory);
        return itemListSize;
    }

    public Item findById(int id) {
        return repository.findById(id);
    }

}
