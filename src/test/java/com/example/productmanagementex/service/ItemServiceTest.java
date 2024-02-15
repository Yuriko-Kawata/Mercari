package com.example.productmanagementex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.Item;
import com.example.productmanagementex.form.CategoryForm;
import com.example.productmanagementex.form.ItemForm;
import com.example.productmanagementex.repository.ItemRepository;

@SpringBootTest
@Transactional
public class ItemServiceTest {
    @Mock
    private ItemRepository repository;

    @InjectMocks
    private ItemService service;

    @Test
    public void testFindAllItems() {
        // Arrange
        List<Item> expectedItems = Arrays.asList(new Item(), new Item());
        when(repository.findAllItems()).thenReturn(expectedItems);

        // Act
        List<Item> actualItems = service.findAllItems();

        // Assert
        assertEquals(expectedItems, actualItems);
        verify(repository).findAllItems();
    }

    @Test
    public void testFindItems() {
        String sort = "test";
        String order = "ASC";
        int page = 1;
        List<Item> itemList = new ArrayList<>();

        when(repository.findAllItems()).thenReturn(itemList);

        assertEquals(itemList, service.findItems(sort, order, page));
    }

    @Test
    public void testTotalItem() {
        when(repository.itemListSize()).thenReturn(1);

        assertEquals(1, service.totalItem());
    }

    @Test
    public void testSearchAllItems() {
        String name = "test";
        String emptyName = null;
        String brand = "test";
        String emptyBrand = null;
        String parentCategory = "parent";
        String childCategory = "child";
        String grandCategory = "grand";
        String nameAll = "parent/child/grand";
        List<Item> itemList = new ArrayList<>();
        List<Item> emptyItemList = new ArrayList<>();

        when(repository.searchAllItems(name, brand, nameAll)).thenReturn(itemList);
        when(repository.searchAllItems(emptyName, emptyBrand, nameAll)).thenReturn(emptyItemList);

        assertEquals(itemList, service.searchAllItems(name, brand, parentCategory, childCategory, grandCategory));
        assertEquals(emptyItemList,
                service.searchAllItems(emptyName, emptyBrand, parentCategory, childCategory, grandCategory));
    }

    @Test
    public void testSearchItems() {
        String name = "test";
        String emptyName = null;
        String brand = "test";
        String emptyBrand = null;
        String sort = "test";
        String order = "ASC";
        String parentCategory = "parent";
        String emptyParentCategory = "";
        String nullParentCategory = null;
        String childCategory = "child";
        String emptyChildCategory = "";
        String nullChildCategory = null;
        String grandCategory = "grand";
        String emptyGrandCategory = "";
        String nullGrandCategory = null;
        String nameAll = "parent/child/grand";
        int page = 1;
        List<Item> itemList = new ArrayList<>();
        List<Item> emptyItemList = new ArrayList<>();

        when(repository.searchItems(name, brand, nameAll, sort, order, page)).thenReturn(itemList);
        when(repository.searchItems(emptyName, emptyBrand, nameAll, sort, order, page)).thenReturn(emptyItemList);

        assertEquals(itemList,
                service.searchItems(name, brand, parentCategory, childCategory, grandCategory, sort, order, page));
        assertEquals(itemList,
                service.searchItems(name, brand, parentCategory, childCategory, emptyGrandCategory, sort, order, page));
        assertEquals(itemList,
                service.searchItems(name, brand, parentCategory, childCategory, nullGrandCategory, sort, order, page));
        assertEquals(itemList,
                service.searchItems(name, brand, parentCategory, emptyChildCategory, emptyGrandCategory, sort, order,
                        page));
        assertEquals(itemList,
                service.searchItems(name, brand, parentCategory, nullChildCategory, nullGrandCategory, sort, order,
                        page));
        assertEquals(itemList,
                service.searchItems(name, brand, emptyParentCategory, emptyChildCategory, emptyGrandCategory, sort,
                        order,
                        page));
        assertEquals(itemList,
                service.searchItems(name, brand, nullParentCategory, nullChildCategory, nullGrandCategory, sort,
                        order,
                        page));
        assertEquals(emptyItemList,
                service.searchItems(emptyName, emptyBrand, parentCategory, childCategory, grandCategory, sort, order,
                        page));
    }

    @Test
    public void testSearchTotalItem() {
        String name = "test";
        String nullName = null;
        String emptyName = "";
        String brand = "test";
        String nullBrand = null;
        String emptyBrand = "";
        String parentCategory = "parent";
        String childCategory = "child";
        String grandCategory = "grand";
        String nameAll = "parent/child/grand";

        when(repository.searchItemsSize(name, brand, nameAll)).thenReturn(0);
        when(repository.searchItemsSize(nullName, nullBrand, nameAll)).thenReturn(0);
        when(repository.searchItemsSize(emptyName, emptyBrand, nameAll)).thenReturn(0);

        assertEquals(0, service.searchTotalItem(name, brand, parentCategory, childCategory, grandCategory));
        assertEquals(0, service.searchTotalItem(nullName, nullBrand, parentCategory, childCategory, grandCategory));
        assertEquals(0, service.searchTotalItem(emptyName, emptyBrand, parentCategory, childCategory, grandCategory));
    }

    @Test
    public void testFindById() {
        int id = 1;
        Item item = new Item();

        when(repository.findById(id)).thenReturn(item);

        assertEquals(item, service.findById(id));
    }

    @Test
    public void testAddItem() {
        ItemForm itemForm = new ItemForm();
        CategoryForm categoryForm = new CategoryForm();
        String nameAll = "test";
        Item item = new Item();

        when(repository.insertItem(item, nameAll)).thenReturn(0);

        assertEquals(0, service.addItem(itemForm, categoryForm));
    }

    @Test
    public void testEditItem() {
        ItemForm itemForm = new ItemForm();

        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setParentCategory("parent");
        categoryForm.setChildCategory("child");
        categoryForm.setGrandCategory("grand");
        String nameAll = "parent/child/grand";

        service.editItem(itemForm, categoryForm);

        verify(repository).updateItem(argThat(item -> {
            return true;
        }), eq(nameAll));
    }

    @Test
    public void testGetUpdateTime() {
        int id = 1;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(repository.getUpdateTime(id)).thenReturn(timestamp);

        assertEquals(timestamp, service.getUpdateTime(id));
    }

    @Test
    public void testCheckDelete() {
        int id = 1;
        int id2 = 2;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        when(repository.checkDelete(id, timestamp)).thenReturn(1);
        when(repository.checkDelete(id2, timestamp)).thenReturn(0);

        assertEquals(true, service.checkDelete(id, timestamp));
        assertEquals(false, service.checkDelete(id2, timestamp));
    }

    @Test
    public void testDelete() {
        int id = 1;

        service.delete(id);

        verify(repository, times(1)).changeDeleteStatus(id);
    }

    @Test
    public void testCountItemByCategory() {
        int id = 1;

        when(repository.countItemByCategory(id)).thenReturn(1);

        assertEquals(1, service.countItemByCategory(id));
    }
}
