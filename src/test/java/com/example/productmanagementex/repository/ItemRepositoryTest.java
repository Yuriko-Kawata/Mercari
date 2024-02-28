package com.example.productmanagementex.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.Item;

@SpringBootApplication
@Transactional
@ExtendWith(MockitoExtension.class)
public class ItemRepositoryTest {
    @Mock
    private NamedParameterJdbcTemplate template;
    @InjectMocks
    private ItemRepository repository;

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void findAllItems_ReturnsListOfItems() {
        // Arrange
        Item expectedItem = new Item(); // Itemオブジェクトを適切に設定
        List<Item> expectedItems = Arrays.asList(expectedItem);
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedItems);

        // Act
        List<Item> actualItems = repository.findAllItems();

        // Assert
        assertNotNull(actualItems);
        assertFalse(actualItems.isEmpty());
        assertEquals(expectedItems, actualItems);
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void findItems_ReturnsItemsBasedOnSortOrderAndPage() {
        // Arrange
        String sort = "name";
        String order = "ASC";
        int page = 1;
        List<Item> expectedItems = Arrays.asList(new Item()); // 適切にItemオブジェクトを設定

        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedItems);

        // Act
        List<Item> actualItems = repository.findItems(sort, order, page);

        // Assert
        assertNotNull(actualItems);
        assertEquals(expectedItems, actualItems);
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void itemListSize_ReturnsCorrectSize() {
        // Arrange
        Integer expectedSize = 10;
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedSize);

        // Act
        Integer actualSize = repository.itemListSize();

        // Assert
        assertNotNull(actualSize);
        assertEquals(expectedSize, actualSize);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void searchAllItems_ReturnsItemList() {
        // Arrange
        String name = "testName";
        String brand = "testBrand";
        String nameAll = "testNameAll";
        List<Item> expectedItems = Arrays.asList(new Item()); // 適切にItemオブジェクトを設定

        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedItems);

        // Act
        List<Item> actualItems = repository.searchAllItems(name, brand, nameAll);

        // Assert
        assertNotNull(actualItems);
        assertEquals(expectedItems, actualItems);
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void searchItems_ReturnsItemsBasedOnCriteria() {
        // Arrange
        String name = "%testName%";
        String brand = "%testBrand%";
        String nameAll = "%testNameAll%";
        String sort = "name";
        String order = "ASC";
        int page = 1;
        List<Item> expectedItems = Arrays.asList(new Item()); // 適切にItemオブジェクトを設定

        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedItems);

        // Act
        List<Item> actualItems = repository.searchItems(name, brand, nameAll, sort, order, page);

        // Assert
        assertNotNull(actualItems);
        assertEquals(expectedItems, actualItems);
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void searchItemsSize_ReturnsCorrectSize() {
        // Arrange
        String name = "%testName%";
        String brand = "%testBrand%";
        String nameAll = "%testNameAll%";
        Integer expectedSize = 10;

        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedSize);

        // Act
        Integer actualSize = repository.searchItemsSize(name, brand, nameAll);

        // Assert
        assertNotNull(actualSize);
        assertEquals(expectedSize, actualSize);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void findById_ReturnsItem() {
        // Arrange
        int id = 1;
        Item expectedItem = new Item(); // 適切にItemオブジェクトを設定
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(Arrays.asList(expectedItem));

        // Act
        Item actualItem = repository.findById(id);

        // Assert
        assertNotNull(actualItem);
        assertEquals(expectedItem, actualItem);
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void findById_WhenNoItemExists_ThrowsException() {
        // Arrange
        int id = 1;
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(Arrays.asList());

        // Act & Assert
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> repository.findById(id));
        assertNotNull(exception);
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void insertItem_ReturnsGeneratedId() {
        // Arrange
        Item item = new Item(); // アイテムのプロパティを適切に設定
        String nameAll = "testNameAll";
        Integer expectedKey = 1;

        doAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(2);
            Map<String, Object> keys = new HashMap<>();
            keys.put("id", expectedKey);
            keyHolder.getKeyList().add(keys);
            return null;
        }).when(template).update(anyString(), any(SqlParameterSource.class), any(KeyHolder.class), any(String[].class));

        // Act
        Integer actualKey = repository.insertItem(item, nameAll);

        // Assert
        assertNotNull(actualKey);
        assertEquals(expectedKey, actualKey);
        verify(template, times(1)).update(anyString(), any(SqlParameterSource.class), any(KeyHolder.class),
                any(String[].class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void updateItem_CallsUpdateWithCorrectParameters() {
        // Arrange
        Item item = new Item(); // Itemオブジェクトのプロパティを適切に設定
        String nameAll = "testNameAll";

        // Act
        repository.updateItem(item, nameAll);

        // Assert
        verify(template, times(1)).update(anyString(), any(SqlParameterSource.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void getUpdateTime_ReturnsCorrectTimestamp() {
        // Arrange
        int id = 1;
        Timestamp expectedUpdateTime = new Timestamp(System.currentTimeMillis());
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Timestamp.class)))
                .thenReturn(expectedUpdateTime);

        // Act
        Timestamp actualUpdateTime = repository.getUpdateTime(id);

        // Assert
        assertNotNull(actualUpdateTime);
        assertEquals(expectedUpdateTime, actualUpdateTime);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Timestamp.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void checkDelete_ReturnsCorrectCount() {
        // Arrange
        int id = 1;
        Timestamp updateTime = new Timestamp(System.currentTimeMillis());
        Integer expectedCount = 1; // 期待されるカウント値
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedCount);

        // Act
        Integer actualCount = repository.checkDelete(id, updateTime);

        // Assert
        assertNotNull(actualCount);
        assertEquals(expectedCount, actualCount);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void changeDeleteStatus_CallsUpdateWithCorrectParameters() {
        // Arrange
        int id = 1;

        // Act
        repository.changeDeleteStatus(id);

        // Assert
        verify(template, times(1)).update(anyString(), any(SqlParameterSource.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void countItemByCategory_ReturnsCorrectItemCount() {
        // Arrange
        int category = 1;
        Integer expectedItemCount = 10;
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedItemCount);

        // Act
        Integer actualItemCount = repository.countItemByCategory(category);

        // Assert
        assertNotNull(actualItemCount);
        assertEquals(expectedItemCount, actualItemCount);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }
}
