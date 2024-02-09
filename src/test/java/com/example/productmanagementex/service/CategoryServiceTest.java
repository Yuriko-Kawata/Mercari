package com.example.productmanagementex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.productmanagementex.domain.Category;
import com.example.productmanagementex.form.CategoryForm;
import com.example.productmanagementex.repository.CategoryRepository;
import com.example.productmanagementex.repository.ItemRepository;

public class CategoryServiceTest {
    @Mock
    private CategoryRepository repository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckCategory() {
        // Arrange
        CategoryForm testForm1 = new CategoryForm();
        testForm1.setParentCategory("Parent");
        testForm1.setChildCategory("Child");
        testForm1.setGrandCategory("Grand");

        // Act
        int actualCount = categoryService.checkCategory(testForm1);

        // Assert
        assertEquals(0, actualCount);
    }

    @Test
    void testCheckCategoryName() {
        // Arrange
        String testName = "Test Name";
        int testParentId = 1;
        int testParentId2 = 0;
        String testNameAll = "";
        String testNameAll2 = "test";
        int expectedParentCondition = 1;
        int expectedParentCondition2 = 2;
        int expectedParentCondition3 = 0;
        int expectedCount = 1;
        int expectedCount2 = 2;
        int expectedCount3 = 3;
        when(repository.checkCategoryName(testName, expectedParentCondition)).thenReturn(expectedCount);
        when(repository.checkCategoryName(testName, expectedParentCondition2)).thenReturn(expectedCount2);
        when(repository.checkCategoryName(testName, expectedParentCondition3)).thenReturn(expectedCount3);

        // Act
        int actualCount = categoryService.checkCategoryName(testName, testParentId, testNameAll);
        int actualCount2 = categoryService.checkCategoryName(testName, testParentId, testNameAll2);
        int actualCount3 = categoryService.checkCategoryName(testName, testParentId2, testNameAll2);

        // Assert
        assertEquals(expectedCount, actualCount);
        assertEquals(expectedCount2, actualCount2);
        assertEquals(expectedCount3, actualCount3);
    }

    @Test
    public void testCheckDeleteCategory_NoChildCategoriesAndNoItems() {
        // Arrange
        int testId = 1;
        int testParentId = 1;
        String testNameAll = "";
        List<Category> emptyCategoryList = new ArrayList<>();
        int zeroItemCount = 0;

        when(repository.findChildCategory(testId)).thenReturn(emptyCategoryList);
        when(itemService.countItemByCategory(testId)).thenReturn(zeroItemCount);

        // Act
        boolean result = categoryService.checkDeleteCategory(testId, testParentId, testNameAll);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCheckDeleteCategory_HasChildCategories() {
        // Arrange
        int testId = 1;
        int testParentId = 0;
        String testNameAll = "";
        String testNameAll2 = "test";
        List<Category> nonEmptyCategoryList = Arrays.asList(new Category(), new Category());

        when(repository.findChildCategory(testId)).thenReturn(nonEmptyCategoryList);
        when(itemService.countItemByCategory(testId)).thenReturn(1);

        // Act
        boolean result = categoryService.checkDeleteCategory(testId, testParentId, testNameAll);
        boolean result2 = categoryService.checkDeleteCategory(testId, testParentId, testNameAll2);

        // Assert
        assertFalse(result);
        assertFalse(result2);
    }

    @Test
    public void testCheckDeleteCategory_HasItems() {
        // Arrange
        int testId = 1;
        int testParentId = 1;
        String testNameAll = "";
        int nonZeroItemCount = 1;

        when(itemService.countItemByCategory(testId)).thenReturn(nonZeroItemCount);

        // Act
        boolean result = categoryService.checkDeleteCategory(testId, testParentId, testNameAll);
        // Assert
        assertTrue(result);
    }

    @Test
    void testChildCategoryCount() {
        // Arrange
        int testId = 1;
        int expectedCount = 3;
        when(repository.childCategorySize(testId)).thenReturn(expectedCount);

        // Act
        int actualCount = categoryService.childCategoryCount(testId);

        // Assert
        assertEquals(expectedCount, actualCount);
    }

    @Test
    void testDelete() {
        // Arrange
        int testId = 1;

        // Act
        categoryService.delete(testId);

        // Assert
        // Verify that the repository's delete method was called with the correct
        // argument
        verify(repository).delete(testId);
    }

    // @Test
    // void testEditCategoryNameAndNameAll() {
    // // Arrange
    // int testId = 1;
    // int testId2 = 2;
    // String testName = "New Name";
    // String testName2 = "";
    // int testParentId = 1;
    // int testParentId2 = 0;
    // String testNameAll = "/Old Parent/Old Child/";
    // String expectedOriginalName = "Old Child";
    // String expectedInsertName = "/New Name";
    // String expectedInsertOriginalName = "/Old Child";
    // String expectedOriginalNameLike = "%/Old Child";

    // when(repository.findNameById(testId)).thenReturn(expectedOriginalName);

    // // Act
    // categoryService.editCategoryNameAndNameAll(testId, testName, testParentId,
    // testNameAll);
    // categoryService.editCategoryNameAndNameAll(testId, testName2, testParentId,
    // testNameAll);
    // categoryService.editCategoryNameAndNameAll(testId2, testName, testParentId2,
    // testNameAll);

    // // Assert
    // // Verify that the repository's methods were called with the correct
    // arguments
    // verify(repository).editCategoryName(testId, testName, 1);
    // verify(repository).editCategoryName(testId, testName2, 2);
    // verify(repository).editCategoryName(testId2, testName, 3);
    // verify(repository).editCategoryNameAll(testId, expectedInsertName,
    // expectedInsertOriginalName,
    // expectedOriginalNameLike);

    // }

    @Test
    void testFindAllCategory() {
        // Arrange
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(repository.findAllCategory()).thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = categoryService.findAllCategory();

        // Assert
        assertEquals(expectedCategories, actualCategories);
    }

    @Test
    void testFindAllChildCategory() {
        // Arrange
        int testPage = 1;
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(repository.findAllChildCategory(testPage)).thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = categoryService.findAllChildCategory(testPage);

        // Assert
        assertEquals(expectedCategories, actualCategories);

    }

    @Test
    void testFindAllGrandCategory() {
        // Arrange
        int testPage = 1;
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(repository.findAllGrandCategory(testPage)).thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = categoryService.findAllGrandCategory(testPage);

        // Assert
        assertEquals(expectedCategories, actualCategories);

    }

    @Test
    void testFindAllParentCategory() {
        // Arrange
        int testPage = 1;
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(repository.findAllParentCategory(testPage)).thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = categoryService.findAllParentCategory(testPage);

        // Assert
        assertEquals(expectedCategories, actualCategories);
    }

    @Test
    void testFindById() {
        // Arrange
        int testId = 1;
        Category expectedCategory = new Category();
        expectedCategory.setId(testId);
        when(repository.findById(testId)).thenReturn(expectedCategory);

        // Act
        Category actualCategory = categoryService.findById(testId);

        // Assert
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void testFindChildCategory() {
        // Arrange
        int testParentId = 1;
        Category expectedCategory = new Category();
        expectedCategory.setId(testParentId);
        when(repository.findParentCategory(testParentId)).thenReturn(expectedCategory);

        // Act
        Category actualCategory = categoryService.findParentCategory(testParentId);

        // Assert
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void testFindParentCategory() {
        // Arrange
        int testParentId = 1;
        Category expectedCategory = new Category();
        expectedCategory.setId(testParentId);
        when(repository.findParentCategory(testParentId)).thenReturn(expectedCategory);

        // Act
        Category actualCategory = categoryService.findParentCategory(testParentId);

        // Assert
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void testInsertCategory() {
        // Arrange
        CategoryForm testForm = new CategoryForm();
        testForm.setParentCategory("Parent");
        testForm.setChildCategory("Child");
        testForm.setGrandCategory("Grand");
        String expectedNameAll = "Parent/Child/Grand";

        // Act
        categoryService.insertCategory(testForm);

        // Assert
        // Verify that the repository's insertCategory method was called with the
        // correct argument
        verify(repository).insertCategory(expectedNameAll);
    }

    @Test
    void testSearchChildCategory() {
        // Arrange
        String testSearchCondition = "test";
        int testPage = 1;
        List<Category> expectedCategories = new ArrayList<>();
        when(repository.searchChildCategory(testSearchCondition, testPage)).thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = categoryService.searchChildCategory(testSearchCondition, testPage);

        // Assert
        assertEquals(expectedCategories, actualCategories);
    }

    @Test
    void testSearchGrandCategory() {
        // Arrange
        String testSearchCondition = "test";
        int testPage = 1;
        List<Category> expectedCategories = new ArrayList<>();
        when(repository.searchGrandCategory(testSearchCondition, testPage)).thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = categoryService.searchGrandCategory(testSearchCondition, testPage);

        // Assert
        assertEquals(expectedCategories, actualCategories);
    }

    @Test
    void testSearchParentCategory() {
        // Arrange
        String testSearchCondition = "test";
        int testPage = 1;
        List<Category> expectedCategories = new ArrayList<>();
        when(repository.searchParentCategory(testSearchCondition, testPage)).thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = categoryService.searchParentCategory(testSearchCondition, testPage);

        // Assert
        assertEquals(expectedCategories, actualCategories);
    }

    @Test
    void testSearchTotalChildCount() {
        // Arrange
        String testSearchCondition = "test";
        String expectedNameLike = "%/test/%";
        int expectedTotalSize = 0;
        when(repository.searchParentTotal(expectedNameLike)).thenReturn(expectedTotalSize);

        // Act
        int actualTotalSize = categoryService.searchTotalParentCount(testSearchCondition);

        // Assert
        assertEquals(expectedTotalSize, actualTotalSize);
    }

    @Test
    void testSearchTotalGrandCount() {
        // Arrange
        String testSearchCondition = "test";
        String expectedNameLike = "%/test";
        int expectedTotalSize = 0;
        when(repository.searchParentTotal(expectedNameLike)).thenReturn(expectedTotalSize);

        // Act
        int actualTotalSize = categoryService.searchTotalParentCount(testSearchCondition);

        // Assert
        assertEquals(expectedTotalSize, actualTotalSize);
    }

    @Test
    void testSearchTotalParentCount() {
        // Arrange
        String testSearchCondition = "test";
        String expectedNameLike = "test/%";
        int expectedTotalSize = 0;
        when(repository.searchParentTotal(expectedNameLike)).thenReturn(expectedTotalSize);

        // Act
        int actualTotalSize = categoryService.searchTotalParentCount(testSearchCondition);

        // Assert
        assertEquals(expectedTotalSize, actualTotalSize);

    }

    @Test
    void testTotalChildCount() {
        // Arrange
        int expectedTotalSize = 10;
        when(repository.childListSize()).thenReturn(expectedTotalSize);

        // Act
        int actualTotalSize = categoryService.totalChildCount();

        // Assert
        assertEquals(expectedTotalSize, actualTotalSize);

    }

    @Test
    void testTotalGrandCount() {
        // Arrange
        int expectedTotalSize = 10;
        when(repository.grandListSize()).thenReturn(expectedTotalSize);

        // Act
        int actualTotalSize = categoryService.totalGrandCount();

        // Assert
        assertEquals(expectedTotalSize, actualTotalSize);

    }

    @Test
    void testTotalParentCount() {
        // Arrange
        int expectedTotalSize = 10;
        when(repository.parentListSize()).thenReturn(expectedTotalSize);

        // Act
        int actualTotalSize = categoryService.totalParentCount();

        // Assert
        assertEquals(expectedTotalSize, actualTotalSize);
    }
}
