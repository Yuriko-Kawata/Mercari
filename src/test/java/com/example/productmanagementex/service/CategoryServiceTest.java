package com.example.productmanagementex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.example.productmanagementex.domain.Category;
import com.example.productmanagementex.form.CategoryForm;
import com.example.productmanagementex.repository.CategoryRepository;

@SpringBootTest
@Transactional
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllCategory() {
        Category category1 = new Category();
        category1.setId(1);
        category1.setName("Category  1");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Category  2");

        List<Category> expectedCategories = Arrays.asList(category1, category2);

        // Configure the mock to return the expected categories when findAllCategory is
        // called
        when(categoryRepository.findAllCategory()).thenReturn(expectedCategories);

        // Call the method under test
        List<Category> actualCategories = categoryService.findAllCategory();

        // Assert that the returned categories match the expected categories
        assertEquals(expectedCategories, actualCategories);
    }

    @Test
    public void testFindById() {
        int testId = 1;
        Category expectedCategory = new Category();
        expectedCategory.setId(testId);
        expectedCategory.setName("Test Category");

        // Configure the mock to return the expected category when findById is called
        when(categoryRepository.findById(testId)).thenReturn(expectedCategory);

        // Call the method under test
        Category actualCategory = categoryService.findById(testId);

        // Assert that the returned category matches the expected category
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    public void testCheckCategory() {
        CategoryForm form = new CategoryForm();
        form.setParentCategory("Parent");
        form.setChildCategory("Child");
        form.setGrandCategory("Grand");

        String expectedNameAll = "Parent/Child/Grand";
        int expectedCount = 1;

        // Configure the mock to return the expected count when checkCategory is called
        when(categoryRepository.checkCategory(expectedNameAll)).thenReturn(expectedCount);

        // Call the method under test
        int actualCount = categoryService.checkCategory(form);

        // Assert that the returned count matches the expected count
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testCheckCategoryName() {
        String testName = "Test Category";
        int testParentIdParent = 0;
        int testParentIdChild = 1;
        int testParentIdGrand = 2;
        String testEmptyNameAll = "";
        String testNameAll = "Parent/Child/Grand";

        int expectedParentConditionParent = 0;
        int expectedParentConditionChild = 1;
        int expectedParentConditionGrand = 2;
        int expectedCountParent = 0;
        int expectedCountChild = 1;
        int expectedCountGrand = 2;

        when(categoryRepository.checkCategoryName(testName, expectedParentConditionParent))
                .thenReturn(expectedCountParent);
        when(categoryRepository.checkCategoryName(testName, expectedParentConditionChild))
                .thenReturn(expectedCountChild);
        when(categoryRepository.checkCategoryName(testName, expectedParentConditionGrand))
                .thenReturn(expectedCountGrand);

        // Call the method under test
        int actualCount = categoryService.checkCategoryName(testName, testParentIdParent, testEmptyNameAll);
        int actualCount2 = categoryService.checkCategoryName(testName, testParentIdChild, testEmptyNameAll);
        int actualCount3 = categoryService.checkCategoryName(testName, testParentIdGrand, testNameAll);

        // Assert that the returned count matches the expected count
        assertEquals(expectedCountParent, actualCount);
        assertEquals(expectedCountChild, actualCount2);
        assertEquals(expectedCountGrand, actualCount3);
    }

    @Test
    public void testInsertCategory() {
        CategoryForm form = new CategoryForm();
        form.setParentCategory("Parent");
        form.setChildCategory("Child");
        form.setGrandCategory("Grand");

        String expectedNameAll = "Parent/Child/Grand";

        // Call the method under test
        categoryService.insertCategory(form);

        // Verify that the repository's insertCategory method was called with the
        // correct arguments
        verify(categoryRepository, times(1)).insertCategory(expectedNameAll);
    }

    @Test
    public void testEditCategoryNameAndNameAll() {
        int testId = 1;
        int testId2 = 2;
        int testId3 = 3;
        String testName = "Test";
        int testParentIdParent = 0;
        int testParentIdChild = 1;
        int testParentIdGrand = 2;
        String testEmptyNameAll = "";
        String testNameAll = "Parent/Child/Grand";

        String testOriginalName = "Old Category Name";

        String testInsertNameParent = "Test/";
        String testInsertNameChild = "/Test/";
        String testInsertNameGrand = "/Test";
        String testInsertOriginalNameParent = "Old Category Name/";
        String testInsertOriginalNameChild = "/Old Category Name/";
        String testInsertOriginalNameGrand = "/Old Category Name";
        String testInsertOriginalNameLikeParent = "Old Category Name/%";
        String testInsertOriginalNameLikeChild = "%/Old Category Name/%";
        String testInsertOriginalNameLikeGrand = "%/Old Category Name";

        when(categoryRepository.findNameById(testId)).thenReturn(testOriginalName);
        when(categoryRepository.findNameById(testId2)).thenReturn(testOriginalName);
        when(categoryRepository.findNameById(testId3)).thenReturn(testOriginalName);

        // Call the method under test
        categoryService.editCategoryNameAndNameAll(testId, testName, testParentIdParent, testEmptyNameAll);
        categoryService.editCategoryNameAndNameAll(testId2, testName, testParentIdChild, testEmptyNameAll);
        categoryService.editCategoryNameAndNameAll(testId3, testName, testParentIdGrand, testNameAll);

        // Verify that the repository's editCategoryName method was called with the
        // correct arguments
        verify(categoryRepository, times(1)).editCategoryName(testId, testName,
                1);
        verify(categoryRepository, times(1)).editCategoryName(testId2, testName,
                2);
        verify(categoryRepository, times(1)).editCategoryName(testId3, testName,
                3);

        // Verify that the repository's editCategoryNameAll method was called with
        // correct arguments
        verify(categoryRepository, times(1)).editCategoryNameAll(testId, testInsertNameParent,
                testInsertOriginalNameParent, testInsertOriginalNameLikeParent);
        verify(categoryRepository, times(1)).editCategoryNameAll(testId2, testInsertNameChild,
                testInsertOriginalNameChild, testInsertOriginalNameLikeChild);
        verify(categoryRepository, times(1)).editCategoryNameAll(testId3, testInsertNameGrand,
                testInsertOriginalNameGrand, testInsertOriginalNameLikeGrand);
    }

    @Test
    public void testFindAllParentCategory() {
        int testPage = 1;
        List<Category> categoryList = new ArrayList<>();

        when(categoryRepository.findAllParentCategory(testPage)).thenReturn(categoryList);

        assertEquals(categoryList, categoryService.findAllParentCategory(testPage));
    }

    @Test
    public void testFindAllChildCategory() {
        int testPage = 1;
        List<Category> categoryList = new ArrayList<>();

        when(categoryRepository.findAllChildCategory(testPage)).thenReturn(categoryList);

        assertEquals(categoryList, categoryService.findAllChildCategory(testPage));
    }

    @Test
    public void testFindAllGrandCategory() {
        int testPage = 1;
        List<Category> categoryList = new ArrayList<>();

        when(categoryRepository.findAllGrandCategory(testPage)).thenReturn(categoryList);

        assertEquals(categoryList, categoryService.findAllGrandCategory(testPage));
    }

    @Test
    public void testTotalParentCount() {
        when(categoryRepository.parentListSize()).thenReturn(1);

        assertEquals(1, categoryService.totalParentCount());
    }

    @Test
    public void testTotalChildCount() {
        when(categoryRepository.childListSize()).thenReturn(2);

        assertEquals(2, categoryService.totalChildCount());
    }

    @Test
    public void testTotalGrandCount() {
        when(categoryRepository.grandListSize()).thenReturn(3);

        assertEquals(3, categoryService.totalGrandCount());
    }

    @Test
    public void testSearchParentCategory() {
        String searchCondition = "Test";
        int page = 1;
        String nameLike = "%/Test/%";
        List<Category> categoryList = new ArrayList<>();

        when(categoryRepository.searchParentCategory(nameLike, page)).thenReturn(categoryList);

        assertEquals(categoryList, categoryService.searchParentCategory(searchCondition, page));
    }

    @Test
    public void testSearchChildCategory() {
        String searchCondition = "Test";
        int page = 1;
        String nameLike = "%/Test/%";
        List<Category> categoryList = new ArrayList<>();

        when(categoryRepository.searchChildCategory(nameLike, page)).thenReturn(categoryList);

        assertEquals(categoryList, categoryService.searchChildCategory(searchCondition, page));
    }

    @Test
    public void testSearchGrandCategory() {
        String searchCondition = "Test";
        int page = 1;
        String nameLike = "%/Test/%";
        List<Category> categoryList = new ArrayList<>();

        when(categoryRepository.searchGrandCategory(nameLike, page)).thenReturn(categoryList);

        assertEquals(categoryList, categoryService.searchGrandCategory(searchCondition, page));
    }

    @Test
    public void testSearchTotalParentCount() {
        String searchCondition = "Test";
        String nameLike = "%/Test/%";

        when(categoryRepository.searchParentTotal(nameLike)).thenReturn(0);

        assertEquals(0, categoryService.searchTotalParentCount(searchCondition));
    }

    @Test
    public void testSearchTotalChildCount() {
        String searchCondition = "Test";
        String nameLike = "%/Test/%";

        when(categoryRepository.searchChildTotal(nameLike)).thenReturn(0);

        assertEquals(0, categoryService.searchTotalChildCount(searchCondition));
    }

    @Test
    public void testSearchTotalGrandCount() {
        String searchCondition = "Test";
        String nameLike = "%/Test/%";

        when(categoryRepository.searchGrandTotal(nameLike)).thenReturn(0);

        assertEquals(0, categoryService.searchTotalGrandCount(searchCondition));
    }

    @Test
    public void testChildCategoryCount() {
        int id = 1;

        when(categoryRepository.childCategorySize(id)).thenReturn(1);

        assertEquals(1, categoryService.childCategoryCount(id));
    }

    @Test
    public void testFindParentCategory() {
        int parentId = 1;
        Category category = new Category();

        when(categoryRepository.findParentCategory(parentId)).thenReturn(category);

        assertEquals(category, categoryService.findParentCategory(parentId));
    }

    @Test
    public void testFindChildCategory() {
        int parentId = 1;
        List<Category> categoryList = new ArrayList<>();

        when(categoryRepository.findChildCategory(parentId)).thenReturn(categoryList);

        assertEquals(categoryList, categoryService.findChildCategory(parentId));
    }

    @Test
    public void testCheckDeleteCategory() {
        int idParent = 1;
        int idChild = 2;
        int idChildEmpty = 3;
        int idGrand = 4;
        int idGrandEmpty = 5;
        int parentId = 1;
        String nameAll = "Test";
        String emptyNameAll = "";
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category());
        List<Category> emptyCategoryList = new ArrayList<>();

        when(categoryRepository.findChildCategory(idParent)).thenReturn(categoryList);
        when(categoryRepository.findChildCategory(idChild)).thenReturn(categoryList);
        when(categoryRepository.findChildCategory(idChildEmpty)).thenReturn(emptyCategoryList);
        when(itemService.countItemByCategory(idGrand)).thenReturn(1);
        when(itemService.countItemByCategory(idGrandEmpty)).thenReturn(0);

        assertEquals(false, categoryService.checkDeleteCategory(idParent, parentId, emptyNameAll));
        assertEquals(false, categoryService.checkDeleteCategory(idChild, parentId, emptyNameAll));
        assertEquals(true, categoryService.checkDeleteCategory(idChildEmpty, parentId, emptyNameAll));
        assertEquals(false, categoryService.checkDeleteCategory(idGrand, parentId, nameAll));
        assertEquals(true, categoryService.checkDeleteCategory(idGrandEmpty, parentId, nameAll));

    }

    @Test
    public void testDelete() {
        int id = 0;

        categoryService.delete(id);

        verify(categoryRepository, times(1)).delete(id);
    }

}
