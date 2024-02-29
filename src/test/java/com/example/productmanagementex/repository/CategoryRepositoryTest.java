package com.example.productmanagementex.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.Category;

@SpringBootApplication
@Transactional
@ExtendWith(MockitoExtension.class)
public class CategoryRepositoryTest {
    @Mock
    private NamedParameterJdbcTemplate template;
    @InjectMocks
    private CategoryRepository repository;

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void findAllCategory_ReturnsListOfCategories() {
        // Arrange
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = repository.findAllCategory();

        // Assert
        assertNotNull(actualCategories);
        assertFalse(actualCategories.isEmpty());
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertEquals(expectedCategories.get(0).getId(), actualCategories.get(0).getId());
        assertEquals(expectedCategories.get(0).getName(), actualCategories.get(0).getName());
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void findNameById_ReturnsCorrectName() {
        // Arrange
        int id = 1;
        String expectedName = "Test Name";
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(String.class)))
                .thenReturn(expectedName);

        // Act
        String actualName = repository.findNameById(id);

        // Assert
        assertNotNull(actualName);
        assertEquals(expectedName, actualName);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(String.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void findById_ReturnsCategory() {
        // Arrange
        int id = 1;
        Category expectedCategory = new Category();
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(expectedCategory));

        // Act
        Category actualCategory = repository.findById(id);

        // Assert
        assertNotNull(actualCategory);
        assertEquals(expectedCategory.getId(), actualCategory.getId());
        assertEquals(expectedCategory.getName(), actualCategory.getName());
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void checkCategory_ReturnsCategoryCount() {
        // Arrange
        String nameAll = "TestCategoryNameAll";
        Integer expectedCount = 1;
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedCount);

        // Act
        Integer actualCount = repository.checkCategory(nameAll);

        // Assert
        assertNotNull(actualCount);
        assertEquals(expectedCount, actualCount);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void checkCategoryName_WhenParentConditionIs0_ReturnsCount() {
        // Arrange
        String name = "TestCategory";
        int parentCondition = 0;
        Integer expectedCount = 1;
        when(template.queryForObject(contains("parent_id IS NULL"), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedCount);

        // Act
        Integer actualCount = repository.checkCategoryName(name, parentCondition);

        // Assert
        assertEquals(expectedCount, actualCount);
    }

    @SuppressWarnings({ "null" })
    @Test
    void checkCategoryName_WhenParentConditionIs1_ReturnsCount() {
        // Arrange
        String name = "TestCategory";
        int parentCondition = 1;
        Integer expectedCount = 2;
        when(template.queryForObject(contains("parent_id IS NOT NULL AND name_all IS NULL"),
                any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedCount);

        // Act
        Integer actualCount = repository.checkCategoryName(name, parentCondition);

        // Assert
        assertEquals(expectedCount, actualCount);
    }

    @SuppressWarnings({ "null" })
    @Test
    void checkCategoryName_WhenParentConditionIs2_ReturnsCount() {
        // Arrange
        String name = "TestCategory";
        int parentCondition = 2;
        Integer expectedCount = 3;
        when(template.queryForObject(contains("parent_id IS NOT NULL AND name_all IS NOT NULL"),
                any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedCount);

        // Act
        Integer actualCount = repository.checkCategoryName(name, parentCondition);

        // Assert
        assertEquals(expectedCount, actualCount);
    }

    @SuppressWarnings({ "null" })
    @Test
    void editCategoryName_WhenParentConditionIs1_CallsUpdateWithCorrectSql() {
        // Arrange
        int id = 1;
        String name = "NewCategoryName";
        int parentCondition = 1;

        // Act
        repository.editCategoryName(id, name, parentCondition);

        // Assert
        verify(template, times(1)).update(contains("AND parent_id IS NULL"), any(SqlParameterSource.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void editCategoryName_WhenParentConditionIs2_CallsUpdateWithCorrectSql() {
        // Arrange
        int id = 1;
        String name = "NewCategoryName";
        int parentCondition = 2;

        // Act
        repository.editCategoryName(id, name, parentCondition);

        // Assert
        verify(template, times(1)).update(contains("AND parent_id IS NOT NULL AND name_all IS NULL"),
                any(SqlParameterSource.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void editCategoryName_WhenParentConditionIsOther_CallsUpdateWithCorrectSql() {
        // Arrange
        int id = 1;
        String name = "NewCategoryName";
        int parentCondition = 3; // Assuming this represents "AND parent_id IS NOT NULL AND name_all IS NOT NULL"

        // Act
        repository.editCategoryName(id, name, parentCondition);

        // Assert
        verify(template, times(1)).update(contains("AND parent_id IS NOT NULL AND name_all IS NOT NULL"),
                any(SqlParameterSource.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void editCategoryNameAll_CallsUpdateWithCorrectParameters() {
        // Arrange
        int id = 1;
        String name = "NewCategoryName";
        String originalName = "OldCategoryName";
        String originalNameLike = "OldCategory%";

        ArgumentCaptor<SqlParameterSource> captor = ArgumentCaptor.forClass(SqlParameterSource.class);

        // Act
        repository.editCategoryNameAll(id, name, originalName, originalNameLike);

        // Assert
        verify(template, times(1)).update(anyString(), captor.capture());

        SqlParameterSource param = captor.getValue();
        assertEquals("NewCategoryName", param.getValue("name"));
        assertEquals("OldCategoryName", param.getValue("originalName"));
        assertEquals("OldCategory%", param.getValue("originalNameLike"));
        assertEquals(id, param.getValue("id"));
    }

    @SuppressWarnings({ "null" })
    @Test
    void insertCategory_CallsUpdateWithCorrectParameters() {
        // Arrange
        String nameAll = "CategoryName";

        // Act
        repository.insertCategory(nameAll);

        // Assert
        verify(template, times(1)).update(anyString(), argThat(new ArgumentMatcher<SqlParameterSource>() {
            @Override
            public boolean matches(SqlParameterSource argument) {
                return "CategoryName".equals(argument.getValue("nameAll"));
            }
        }));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void findAllParentCategory_ReturnsListOfCategories() {
        // Arrange
        int page = 1;
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = repository.findAllParentCategory(page);

        // Assert
        assertNotNull(actualCategories);
        assertFalse(actualCategories.isEmpty());
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertEquals(expectedCategories.get(0).getId(), actualCategories.get(0).getId());
        assertEquals(expectedCategories.get(0).getName(), actualCategories.get(0).getName());
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void findAllChildCategory_ReturnsListOfCategories() {
        // Arrange
        int page = 1;
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = repository.findAllChildCategory(page);

        // Assert
        assertNotNull(actualCategories);
        assertFalse(actualCategories.isEmpty());
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertEquals(expectedCategories.get(0).getId(), actualCategories.get(0).getId());
        assertEquals(expectedCategories.get(0).getName(), actualCategories.get(0).getName());
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void findAllGrandCategory_ReturnsListOfCategories() {
        // Arrange
        int page = 1;
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = repository.findAllGrandCategory(page);

        // Assert
        assertNotNull(actualCategories);
        assertFalse(actualCategories.isEmpty());
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertEquals(expectedCategories.get(0).getId(), actualCategories.get(0).getId());
        assertEquals(expectedCategories.get(0).getName(), actualCategories.get(0).getName());
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void parentListSize_ReturnsCorrectSize() {
        // Arrange
        Integer expectedSize = 10;
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedSize);

        // Act
        Integer actualSize = repository.parentListSize();

        // Assert
        assertNotNull(actualSize);
        assertEquals(expectedSize, actualSize);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void childListSize_ReturnsCorrectSize() {
        // Arrange
        Integer expectedSize = 10;
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedSize);

        // Act
        Integer actualSize = repository.childListSize();

        // Assert
        assertNotNull(actualSize);
        assertEquals(expectedSize, actualSize);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void grandListSize_ReturnsCorrectSize() {
        // Arrange
        Integer expectedSize = 10;
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedSize);

        // Act
        Integer actualSize = repository.grandListSize();

        // Assert
        assertNotNull(actualSize);
        assertEquals(expectedSize, actualSize);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void searchParentCategory_ReturnsListOfCategories() {
        // Arrange
        String nameLike = "%test%";
        Integer page = 1;
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = repository.searchParentCategory(nameLike, page);

        // Assert
        assertNotNull(actualCategories);
        assertFalse(actualCategories.isEmpty());
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertEquals(expectedCategories.get(0).getId(), actualCategories.get(0).getId());
        assertEquals(expectedCategories.get(0).getName(), actualCategories.get(0).getName());
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void searchChildCategory_ReturnsListOfCategories() {
        // Arrange
        String nameLike = "%test%";
        Integer page = 1;
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = repository.searchChildCategory(nameLike, page);

        // Assert
        assertNotNull(actualCategories);
        assertFalse(actualCategories.isEmpty());
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertEquals(expectedCategories.get(0).getId(), actualCategories.get(0).getId());
        assertEquals(expectedCategories.get(0).getName(), actualCategories.get(0).getName());
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void searchGrandCategory_ReturnsListOfCategories() {
        // Arrange
        String nameLike = "%test%";
        Integer page = 1;
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = repository.searchGrandCategory(nameLike, page);

        // Assert
        assertNotNull(actualCategories);
        assertFalse(actualCategories.isEmpty());
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertEquals(expectedCategories.get(0).getId(), actualCategories.get(0).getId());
        assertEquals(expectedCategories.get(0).getName(), actualCategories.get(0).getName());
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void searchParentTotal_ReturnsCorrectSize() {
        // Arrange
        String nameLike = "%test%";
        Integer expectedSize = 5;
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedSize);

        // Act
        Integer actualSize = repository.searchParentTotal(nameLike);

        // Assert
        assertNotNull(actualSize);
        assertEquals(expectedSize, actualSize);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void searchChildTotal_ReturnsCorrectSize() {
        // Arrange
        String nameLike = "%test%";
        Integer expectedSize = 5;
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedSize);

        // Act
        Integer actualSize = repository.searchChildTotal(nameLike);

        // Assert
        assertNotNull(actualSize);
        assertEquals(expectedSize, actualSize);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void searchGrandTotal_ReturnsCorrectSize() {
        // Arrange
        String nameLike = "%test%";
        Integer expectedSize = 5;
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedSize);

        // Act
        Integer actualSize = repository.searchGrandTotal(nameLike);

        // Assert
        assertNotNull(actualSize);
        assertEquals(expectedSize, actualSize);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void childCategorySize_ReturnsCorrectSize() {
        // Arrange
        int parentId = 1;
        Integer expectedSize = 10; // Assuming there are 10 child categories for the parent category with ID 1
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .thenReturn(expectedSize);

        // Act
        Integer actualSize = repository.childCategorySize(parentId);

        // Assert
        assertNotNull(actualSize);
        assertEquals(expectedSize, actualSize);
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void findChildCategory_ReturnsListOfCategories() {
        // Arrange
        int parentId = 1;
        List<Category> expectedCategories = Arrays.asList(
                new Category(),
                new Category());
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = repository.findChildCategory(parentId);

        // Assert
        assertNotNull(actualCategories);
        assertFalse(actualCategories.isEmpty());
        assertEquals(expectedCategories.size(), actualCategories.size());
        for (int i = 0; i < expectedCategories.size(); i++) {
            assertEquals(expectedCategories.get(i).getId(), actualCategories.get(i).getId());
            assertEquals(expectedCategories.get(i).getName(), actualCategories.get(i).getName());
        }
        verify(template, times(1)).query(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void findParentCategory_ReturnsCategory() {
        // Arrange
        int parentId = 1;
        Category expectedCategory = new Category();
        when(template.queryForObject(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(expectedCategory);

        // Act
        Category actualCategory = repository.findParentCategory(parentId);

        // Assert
        assertNotNull(actualCategory);
        assertEquals(expectedCategory.getId(), actualCategory.getId());
        assertEquals(expectedCategory.getName(), actualCategory.getName());
        verify(template, times(1)).queryForObject(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }

    @SuppressWarnings({ "null" })
    @Test
    void delete_CallsUpdateWithCorrectParameters() {
        // Arrange
        int id = 1; // Example ID for deletion

        // Act
        repository.delete(id);

        // Assert
        verify(template, times(1)).update(anyString(), argThat(new ArgumentMatcher<SqlParameterSource>() {
            @Override
            public boolean matches(SqlParameterSource argument) {
                return argument.getValue("id").equals(id);
            }
        }));
    }
}
