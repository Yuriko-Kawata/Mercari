package com.example.productmanagementex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.productmanagementex.domain.Category;
import com.example.productmanagementex.form.CategoryForm;
import com.example.productmanagementex.service.CategoryService;
import com.example.productmanagementex.service.ItemService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
    @Mock
    private CategoryService categoryService;
    @Mock
    private ItemService itemService;
    @Mock
    private HttpSession session;
    @Mock
    private Model model;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CategoryController controller;

    @BeforeEach
    private void setup() {
    }

    @Test
    void toCategoryList_SetsSessionAttributesAndReturnsCategoryListView() {
        // Arrange
        int parentPage = 1;
        int childPage = 1;
        int grandPage = 1;
        int totalParentCount = 10; // Example count
        int totalChildCount = 20; // Example count
        int totalGrandCount = 30; // Example count

        // Mocking service methods
        when(categoryService.totalParentCount()).thenReturn(totalParentCount);
        when(categoryService.totalChildCount()).thenReturn(totalChildCount);
        when(categoryService.totalGrandCount()).thenReturn(totalGrandCount);

        // Act
        String viewName = controller.toCategoryList(parentPage, childPage, grandPage, model);

        // Assert
        // Verify session attribute settings
        verify(session).setAttribute(eq("totalParentCategoryCount"), eq(totalParentCount));
        verify(session).setAttribute(eq("totalChildCategoryCount"), eq(totalChildCount));
        verify(session).setAttribute(eq("totalGrandCategoryCount"), eq(totalGrandCount));

        // Verify the return view name
        assertEquals("category-list", viewName);
    }

    @Test
    void toCategoryList_ShouldRemoveSearchConditionFromSessionIfPresent() {
        // Arrange
        int parentPage = 1;
        int childPage = 1;
        int grandPage = 1;
        int totalParentCount = 10; // Example count
        int totalChildCount = 20; // Example count
        int totalGrandCount = 30; // Example count

        // Mocking service methods
        when(categoryService.totalParentCount()).thenReturn(totalParentCount);
        when(categoryService.totalChildCount()).thenReturn(totalChildCount);
        when(categoryService.totalGrandCount()).thenReturn(totalGrandCount);
        when(session.getAttribute("categorySearchCondition")).thenReturn("some search condition");

        // Act
        String viewName = controller.toCategoryList(parentPage, childPage, grandPage, model);

        // Assert
        // Verify session attribute settings
        verify(session).setAttribute(eq("totalParentCategoryCount"), eq(totalParentCount));
        verify(session).setAttribute(eq("totalChildCategoryCount"), eq(totalChildCount));
        verify(session).setAttribute(eq("totalGrandCategoryCount"), eq(totalGrandCount));

        // Verify the return view name
        assertEquals("category-list", viewName);
    }

    @Test
    void toSearchCategory_WithEmptySearchCriteria_RedirectsToCategoryList() {
        // Arrange
        String searchCategory = "";

        // Act
        String viewName = controller.toSearchCategory(searchCategory, 1, 1, 1, model);

        // Assert
        assertEquals("category-list", viewName);
    }

    @Test
    void toSearchCategory_WithNonEmptySearchCriteria_SetsAttributesCorrectly() {
        // Arrange
        String searchCategory = "test";
        int parentPage = 1, childPage = 1, grandPage = 1;
        int totalParentCount = 5, totalChildCount = 10, totalGrandCount = 15;

        when(categoryService.searchTotalParentCount(searchCategory)).thenReturn(totalParentCount);
        when(categoryService.searchTotalChildCount(searchCategory)).thenReturn(totalChildCount);
        when(categoryService.searchTotalGrandCount(searchCategory)).thenReturn(totalGrandCount);

        // Act
        String viewName = controller.toSearchCategory(searchCategory, parentPage, childPage, grandPage, model);

        // Assert
        verify(session).setAttribute(eq("categorySearchCondition"), eq(searchCategory));
        verify(session).setAttribute(eq("totalParentCategoryCount"), eq(totalParentCount));
        verify(session).setAttribute(eq("totalChildCategoryCount"), eq(totalChildCount));
        verify(session).setAttribute(eq("totalGrandCategoryCount"), eq(totalGrandCount));
        assertEquals("category-list", viewName);
    }

    @SuppressWarnings("null")
    @Test
    void toSearchParentPage_WithSearchCondition_SetsAttributesCorrectly() {
        // Arrange
        int parentPage = 1;
        String searchCondition = "test";
        int totalParentCategoryCount = 5;

        when(session.getAttribute("categorySearchCondition")).thenReturn(searchCondition);
        when(categoryService.searchTotalParentCount(searchCondition)).thenReturn(totalParentCategoryCount);

        // Act
        String viewName = controller.toSearchParentPage(parentPage, model);

        // Assert
        verify(model).addAttribute(eq("searchCondition"), eq(searchCondition));
        verify(session).setAttribute(eq("parentCategoryList"), any());
        verify(session).setAttribute(eq("totalParentCategoryCount"), eq(totalParentCategoryCount));
        verify(session).setAttribute(eq("totalParentCategoryPage"), anyInt());
        verify(session).setAttribute(eq("currentParentCategoryPage"), eq(parentPage));
        assertEquals("category-list", viewName);
    }

    @Test
    void toSearchParentPage_WhenSearchConditionIsNull_ReturnsToCategoryList() {
        // Arrange
        int parentPage = 1;
        when(session.getAttribute("categorySearchCondition")).thenReturn(null);
        when(session.getAttribute("currentChildCategoryPage")).thenReturn(1);
        when(session.getAttribute("currentGrandCategoryPage")).thenReturn(1);

        // Act
        String viewName = controller.toSearchParentPage(parentPage, model);

        // Assert
        assertEquals("category-list", viewName);
        verify(session, times(2)).getAttribute("categorySearchCondition");
        verify(session, times(1)).getAttribute("currentChildCategoryPage");
        verify(session, times(1)).getAttribute("currentGrandCategoryPage");
        verify(categoryService, never()).searchParentCategory(anyString(), anyInt());
        verify(categoryService, never()).searchTotalParentCount(anyString());
    }

    @SuppressWarnings("null")
    @Test
    void toSearchChildPage_WithSearchCondition_SetsAttributesCorrectly() {
        // Arrange
        int childPage = 1;
        String searchCondition = "test";
        int totalChildCategoryCount = 5;

        when(session.getAttribute("categorySearchCondition")).thenReturn(searchCondition);
        when(categoryService.searchTotalChildCount(searchCondition)).thenReturn(totalChildCategoryCount);

        // Act
        String viewName = controller.toSearchChildPage(childPage, model);

        // Assert
        verify(model).addAttribute(eq("searchCondition"), eq(searchCondition));
        verify(session).setAttribute(eq("childCategoryList"), any());
        verify(session).setAttribute(eq("totalChildCategoryCount"), eq(totalChildCategoryCount));
        verify(session).setAttribute(eq("totalChildCategoryPage"), anyInt());
        verify(session).setAttribute(eq("currentChildCategoryPage"), eq(childPage));
        assertEquals("category-list", viewName);
    }

    @Test
    void toSearchChildPage_WhenSearchConditionIsNull_ReturnsToCategoryList() {
        // Arrange
        int childPage = 1;
        when(session.getAttribute("categorySearchCondition")).thenReturn(null);
        when(session.getAttribute("currentParentCategoryPage")).thenReturn(1);
        when(session.getAttribute("currentGrandCategoryPage")).thenReturn(1);

        // Act
        String viewName = controller.toSearchChildPage(childPage, model);

        // Assert
        assertEquals("category-list", viewName);
        verify(session, times(2)).getAttribute("categorySearchCondition");
        verify(session, times(1)).getAttribute("currentParentCategoryPage");
        verify(session, times(1)).getAttribute("currentGrandCategoryPage");
        verify(categoryService, never()).searchChildCategory(anyString(), anyInt());
        verify(categoryService, never()).searchTotalChildCount(anyString());
    }

    @SuppressWarnings("null")
    @Test
    void toSearchGrandPage_WithSearchCondition_SetsAttributesCorrectly() {
        // Arrange
        int grandPage = 1;
        String searchCondition = "test";
        int totalGrandCategoryCount = 5;

        when(session.getAttribute("categorySearchCondition")).thenReturn(searchCondition);
        when(categoryService.searchTotalGrandCount(searchCondition)).thenReturn(totalGrandCategoryCount);

        // Act
        String viewName = controller.toSearchGrandPage(grandPage, model);

        // Assert
        verify(model).addAttribute(eq("searchCondition"), eq(searchCondition));
        verify(session).setAttribute(eq("grandCategoryList"), any());
        verify(session).setAttribute(eq("totalGrandCategoryCount"), eq(totalGrandCategoryCount));
        verify(session).setAttribute(eq("totalGrandCategoryPage"), anyInt());
        verify(session).setAttribute(eq("currentGrandCategoryPage"), eq(grandPage));
        assertEquals("category-list", viewName);
    }

    @Test
    void toSearchGrandPage_WhenSearchConditionIsNull_ReturnsToCategoryList() {
        // Arrange
        int grandPage = 1;
        when(session.getAttribute("categorySearchCondition")).thenReturn(null);
        when(session.getAttribute("currentParentCategoryPage")).thenReturn(1);
        when(session.getAttribute("currentChildCategoryPage")).thenReturn(1);

        // Act
        String viewName = controller.toSearchGrandPage(grandPage, model);

        // Assert
        assertEquals("category-list", viewName);
        verify(session, times(2)).getAttribute("categorySearchCondition");
        verify(session, times(1)).getAttribute("currentParentCategoryPage");
        verify(session, times(1)).getAttribute("currentChildCategoryPage");
        verify(categoryService, never()).searchGrandCategory(anyString(), anyInt());
        verify(categoryService, never()).searchTotalGrandCount(anyString());
    }

    @SuppressWarnings("null")
    @Test
    void categoryDetail_WithChildCategories_ShouldAddCategoryDetailsToModel() {
        // Arrange
        int categoryId = 1;
        when(categoryService.findById(categoryId)).thenReturn(new Category()); // Mock finding category
        when(categoryService.findChildCategory(categoryId)).thenReturn(List.of(new Category())); // Mock finding child
                                                                                                 // categories
        when(categoryService.childCategoryCount(categoryId)).thenReturn(1); // Assume there is at least one child
                                                                            // category

        // Act
        String viewName = controller.categoryDetail(categoryId, model);

        // Assert
        verify(model, times(1)).addAttribute(eq("category"), any());
        verify(model, times(1)).addAttribute(eq("childCategoryList"), any());
        verify(model, times(1)).addAttribute(eq("childCategoryCount"), eq(1));
        assertEquals("category-detail", viewName);
    }

    @SuppressWarnings("null")
    @Test
    void categoryDetail_WithNoChildCategories_ShouldAddItemCountToModel() {
        // Arrange
        int categoryId = 1;
        when(categoryService.findById(categoryId)).thenReturn(new Category()); // Mock finding category
        when(categoryService.childCategoryCount(categoryId)).thenReturn(0); // Assume there are no child categories
        when(itemService.countItemByCategory(categoryId)).thenReturn(10); // Mock item count for the category

        // Act
        String viewName = controller.categoryDetail(categoryId, model);

        // Assert
        verify(model, times(1)).addAttribute(eq("category"), any());
        verify(model, times(1)).addAttribute(eq("childCategoryCount"), eq(0));
        verify(model, times(1)).addAttribute(eq("itemCount"), eq(10));
        assertEquals("category-detail", viewName);
    }

    @Test
    void toAddCategory_SetsModelAttributes_AndReturnsAddCategoryView() {
        // Arrange
        CategoryForm categoryForm = new CategoryForm(); // Assuming CategoryForm is your form backing object
        List<Category> allCategories = List.of(new Category(), new Category()); // Mock some categories

        when(categoryService.findAllCategory()).thenReturn(allCategories);

        // Act
        String viewName = controller.toAddCategory(categoryForm, model);

        // Assert
        verify(model).addAttribute("categoryForm", categoryForm);
        verify(model).addAttribute("categoryList", allCategories);
        assertEquals("category-add", viewName);
    }

    @Test
    void addCategory_WithValidationErrors_ShouldReturnToAddCategory() {
        // Arrange
        CategoryForm categoryForm = new CategoryForm();
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String viewName = controller.addCategory(categoryForm, bindingResult, model);

        // Assert
        assertEquals("category-add", viewName);
    }

    @SuppressWarnings("null")
    @Test
    void addCategory_WithExistingCategory_ShouldReturnToAddCategoryWithError() {
        // Arrange
        CategoryForm categoryForm = new CategoryForm();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(categoryService.checkCategory(categoryForm)).thenReturn(1);
        when(messageSource.getMessage(eq("error.check.existCategory"), isNull(), eq(Locale.getDefault())))
                .thenReturn("Category already exists");

        // Act
        String viewName = controller.addCategory(categoryForm, bindingResult, model);

        // Assert
        verify(model).addAttribute("error", "Category already exists");
        assertEquals("category-add", viewName);
    }

    @Test
    void addCategory_SuccessfulAddition_ShouldRedirectToConfirmation() {
        // Arrange
        CategoryForm categoryForm = new CategoryForm();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(categoryService.checkCategory(categoryForm)).thenReturn(0);

        // Act
        String viewName = controller.addCategory(categoryForm, bindingResult, model);

        // Assert
        assertEquals("confirm/add-category-confirm", viewName);
    }

    @Test
    void toEditCategory_AddsCategoryDataToModel_AndReturnsEditView() {
        // Arrange
        int categoryId = 1;
        Category category = new Category(); // Assuming Category is your domain class
        category.setId(categoryId);
        when(categoryService.findById(categoryId)).thenReturn(category);

        // Act
        String viewName = controller.toEditCategory(categoryId, model);

        // Assert
        verify(model).addAttribute("categoryData", category);
        assertEquals("category-edit", viewName);
    }

    @Test
    void editCategory_WithEmptyName_ShouldReturnToEditWithError() {
        // Arrange
        CategoryForm form = new CategoryForm();
        form.setName("");

        // Act
        String viewName = controller.editCategory(form, model);

        // Assert
        verify(model).addAttribute("inputError", "nameを入力してください");
        assertEquals("category-edit", viewName);
    }

    @SuppressWarnings("null")
    @Test
    void editCategory_WithExistingCategoryCombination_ShouldReturnToEditWithError() {
        // Arrange
        CategoryForm form = new CategoryForm();
        form.setId(1);
        form.setName("Existing Name");
        form.setParentId(0); // Assuming root category
        form.setNameAll("Existing/Name");

        when(categoryService.checkCategoryName(form.getName(), form.getParentId(), form.getNameAll())).thenReturn(1);
        when(messageSource.getMessage(eq("error.check.existCategory"), isNull(), eq(Locale.getDefault())))
                .thenReturn("Category combination exists");

        // Act
        String viewName = controller.editCategory(form, model);

        // Assert
        verify(model).addAttribute("checkError", "Category combination exists");
        assertEquals("category-edit", viewName);
    }

    @Test
    void editCategory_SuccessfulEdit_ShouldRedirectToConfirmation() {
        // Arrange
        CategoryForm form = new CategoryForm();
        form.setId(1);
        form.setName("New Name");
        form.setParentId(0); // Assuming root category
        form.setNameAll("New/Name");

        when(categoryService.checkCategoryName(form.getName(), form.getParentId(), form.getNameAll())).thenReturn(0);

        // Act
        String viewName = controller.editCategory(form, model);

        // Assert
        verify(categoryService).editCategoryNameAndNameAll(form.getId(), form.getName(), form.getParentId(),
                form.getNameAll());
        verify(model).addAttribute("categoryId", form.getId());
        assertEquals("confirm/edit-category-confirm", viewName);
    }

    @SuppressWarnings("null")
    @Test
    void deleteCategory_WhenCheckDeleteFails_ShouldReturnToEditWithError() {
        // Arrange
        int id = 1;
        int parentId = 0;
        String nameAll = "Test/Category";

        when(categoryService.checkDeleteCategory(id, parentId, nameAll)).thenReturn(false);
        when(messageSource.getMessage(eq("error.checkDeleteCategory"), isNull(), eq(Locale.getDefault())))
                .thenReturn("Cannot delete this category");

        // Act
        String viewName = controller.deleteCategory(id, parentId, nameAll, model);

        // Assert
        verify(model).addAttribute("deleteError", "Cannot delete this category");
        assertEquals("category-edit", viewName);
    }

    @Test
    void deleteCategory_WhenDeletionIsSuccessful_ShouldRedirectToConfirmation() {
        // Arrange
        int id = 1;
        int parentId = 0;
        String nameAll = "Test/Category";

        when(categoryService.checkDeleteCategory(id, parentId, nameAll)).thenReturn(true);

        // Act
        String viewName = controller.deleteCategory(id, parentId, nameAll, model);

        // Assert
        verify(categoryService).delete(id);
        assertEquals("confirm/delete-category-confirm", viewName);
    }
}
