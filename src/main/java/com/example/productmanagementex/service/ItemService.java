package com.example.productmanagementex.service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Item> findAllItems() {
        List<Item> itemList = repository.findAllItems();
        List<Item> top30List = itemList.stream().limit(30).collect(Collectors.toList());

        return top30List;
    }

    public List<Item> searchItems(String name, String brand, String parentCategory, String childCategory,
            String grandCategory) {
        List<Item> itemList = repository.searchItems(name, brand, parentCategory, childCategory, grandCategory);
        List<Item> top30List = itemList.stream().limit(30).collect(Collectors.toList());
        return top30List;
    }

}
