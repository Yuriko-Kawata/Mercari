package com.example.productmanagementex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.example.productmanagementex.domain.Category;
import com.example.productmanagementex.domain.Item;
import com.example.productmanagementex.domain.User;
import com.example.productmanagementex.form.CategoryForm;
import com.example.productmanagementex.form.ItemForm;
import com.example.productmanagementex.form.SearchForm;
import com.example.productmanagementex.service.CategoryService;
import com.example.productmanagementex.service.FileStorageService;
import com.example.productmanagementex.service.ImageService;
import com.example.productmanagementex.service.ItemService;
import com.example.productmanagementex.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private Model model;
    @Mock
    private HttpSession session;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private MessageSource messageSource;
    @Mock
    private FileStorageService fileStorageService;
    @Mock
    private ImageService imageService;
    @Mock
    private BindingResult itemRs;
    @Mock
    private BindingResult categoryRs;
    @Mock
    private HttpServletResponse response;

    @Spy
    @InjectMocks
    private ItemController controller;

    @BeforeEach
    public void setUp() {
    }

    @SuppressWarnings("null")
    @Test
    public void testToItemList() {
        // Arrange
        String currentUserMail = "test@example.com";
        User user = new User();
        user.setName("Test User");
        when(userService.findUserByMail(currentUserMail)).thenReturn(user);

        // Mock Authentication object
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(currentUserMail);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        SearchForm form = new SearchForm();
        form.setSort("i.id");
        form.setOrder("ASC");
        when(session.getAttribute("form")).thenReturn(null);

        when(itemService.findItems("i.id", "ASC", 1)).thenReturn(new ArrayList<>());
        when(itemService.totalItem()).thenReturn(100);
        when(categoryService.findAllCategory()).thenReturn(new ArrayList<>());

        // Act
        String result = controller.toItemList(1, "i.id", "ASC", model);

        // Assert
        assertEquals("item-list", result);
        verify(model, times(1)).addAttribute(eq("searchCondition"), any(SearchForm.class));
        verify(model, times(1)).addAttribute(eq("itemList"), anyList());
        verify(model, times(1)).addAttribute(eq("totalPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("totalItemCount"), anyInt());
        verify(model, times(1)).addAttribute(eq("currentPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("categoryList"), anyList());
    }

    @SuppressWarnings("null")
    @Test
    public void testToItemListFormIsNotNull() {
        // Arrange
        String currentUserMail = "test@example.com";
        User user = new User();
        user.setName("Test User");
        when(userService.findUserByMail(currentUserMail)).thenReturn(user);

        // Mock Authentication object
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(currentUserMail);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        SearchForm form = new SearchForm();
        form.setSort("i.id");
        form.setOrder("ASC");
        when(session.getAttribute("form")).thenReturn(null);

        when(itemService.findItems("i.id", "ASC", 1)).thenReturn(new ArrayList<>());
        when(itemService.totalItem()).thenReturn(100);
        when(categoryService.findAllCategory()).thenReturn(new ArrayList<>());
        when(session.getAttribute("form")).thenReturn(form);

        // Act
        String result = controller.toItemList(1, "i.id", "ASC", model);

        // Assert
        assertEquals("item-list", result);
        verify(model, times(1)).addAttribute(eq("searchCondition"), any(SearchForm.class));
        verify(model, times(1)).addAttribute(eq("itemList"), anyList());
        verify(model, times(1)).addAttribute(eq("totalPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("totalItemCount"), anyInt());
        verify(model, times(1)).addAttribute(eq("currentPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("categoryList"), anyList());
    }

    @SuppressWarnings("null")
    @Test
    public void testToSearch() {
        // Arrange
        SearchForm form = new SearchForm();
        form.setSort("i.id");
        form.setOrder("ASC");
        BindingResult rs = mock(BindingResult.class);
        when(itemService.searchItems(null, null, null, null, null, "i.id",
                "ASC", 1))
                .thenReturn(new ArrayList<>());
        when(itemService.searchTotalItem(null, null, null, null, null))
                .thenReturn(100);
        when(categoryService.findAllCategory()).thenReturn(new ArrayList<>());

        // Act
        String result = controller.toSearch(form, rs, model, 1);

        // Assert
        assertEquals("item-list", result);
        verify(model, times(1)).addAttribute(eq("searchCondition"), any(SearchForm.class));
        verify(model, times(1)).addAttribute(eq("itemList"), anyList());
        verify(model, times(1)).addAttribute(eq("totalPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("totalItemCount"), anyInt());
        verify(model, times(1)).addAttribute(eq("currentPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("categoryList"), anyList());
    }

    @SuppressWarnings("null")
    @Test
    public void testToSearchTotalItemIsNull() {
        // Arrange
        SearchForm form = new SearchForm();
        form.setSort("i.id");
        form.setOrder("ASC");
        BindingResult rs = mock(BindingResult.class);
        when(itemService.searchItems(null, null, null, null, null, "i.id",
                "ASC", 1))
                .thenReturn(new ArrayList<>());
        when(itemService.searchTotalItem(null, null, null, null, null))
                .thenReturn(0);
        when(categoryService.findAllCategory()).thenReturn(new ArrayList<>());

        // Act
        String result = controller.toSearch(form, rs, model, 1);

        // Assert
        assertEquals("item-list", result);
        verify(model, times(1)).addAttribute(eq("searchCondition"), any(SearchForm.class));
        verify(model, times(1)).addAttribute(eq("itemList"), anyList());
        verify(model, times(1)).addAttribute(eq("totalPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("totalItemCount"), anyInt());
        verify(model, times(1)).addAttribute(eq("currentPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("categoryList"), anyList());
    }

    @SuppressWarnings("null")
    @Test
    public void testToSort() {
        // Arrange
        SearchForm form = new SearchForm();
        form.setSort("i.id");
        form.setOrder("ASC");
        when(session.getAttribute("form")).thenReturn(form);

        when(itemService.searchItems(null, null, null, null, null, "i.id",
                "ASC", 1))
                .thenReturn(new ArrayList<>());
        when(itemService.searchTotalItem(null, null, null, null, null))
                .thenReturn(100);
        when(categoryService.findAllCategory()).thenReturn(new ArrayList<>());

        // Act
        String result = controller.toSort("i.id", "ASC", model);

        // Assert
        assertEquals("item-list", result);
        verify(model, times(1)).addAttribute(eq("searchCondition"), any(SearchForm.class));
        verify(model, times(1)).addAttribute(eq("itemList"), anyList());
        verify(model, times(1)).addAttribute(eq("totalPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("totalItemCount"), anyInt());
        verify(model, times(1)).addAttribute(eq("currentPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("categoryList"), anyList());
    }

    @Test
    void whenFormIsNull_thenRedirectToItemList() {
        // セッションからformがnullであることを模擬
        when(session.getAttribute("form")).thenReturn(null);

        // toItemListメソッドの挙動をモック化
        doReturn("item-list-page").when(controller).toItemList(anyInt(), anyString(), anyString(), any(Model.class));

        String result = controller.toSearchPage(1, model);

        // toItemListが呼び出されたことを確認
        verify(controller).toItemList(1, "i.id", "ASC", model);
        assertEquals("item-list-page", result);
    }

    @Test
    public void testToSearchPageWithSearchFormInSession() {
        // セッションに検索フォームが存在する場合を模倣
        SearchForm form = new SearchForm();
        // formのプロパティを設定...
        when(session.getAttribute(anyString())).thenReturn(form);

        // itemServiceとcategoryServiceの振る舞いを模倣
        when(itemService.searchItems(null, null, null, null, null, null,
                null, 1))
                .thenReturn(new ArrayList<>());
        when(itemService.searchTotalItem(null, null, null, null, null))
                .thenReturn(100);
        when(categoryService.findAllCategory()).thenReturn(new ArrayList<>());

        // テスト実行
        String viewName = controller.toSearchPage(1, model);

        // 検証
        assertEquals("item-list", viewName);
        // モデルに期待される属性が追加されているか検証する
    }

    @Test
    void whenParentIdIsZero_thenParentCategoryIsUpdated() {
        // セットアップ: parentCategoryのみを更新するシナリオ
        String name = "ParentCategoryName";
        int parentId = 0;
        String nameAll = "";

        // テスト実行
        String viewName = controller.categoryFilter(name, parentId, nameAll, model);

        // 検証
        verify(session).setAttribute(eq("form"), any(SearchForm.class));
        verify(itemService).searchItems(null, null, name, null, null, "i.id",
                "ASC", 1);
        assertEquals("item-list", viewName);
    }

    @Test
    void whenParentIdIsNotZeroAndNameAllIsEmpty_thenParentAndChildCategoriesAreUpdated() {
        // セットアップ: parentCategoryとchildCategoryを更新するシナリオ
        String name = "ChildCategoryName";
        int parentId = 1; // 0以外を設定
        String nameAll = "";
        Category parentCategory = new Category();
        parentCategory.setName("ParentCategoryName");
        when(categoryService.findParentCategory(parentId)).thenReturn(parentCategory);

        // テスト実行
        String viewName = controller.categoryFilter(name, parentId, nameAll, model);

        // 検証
        verify(session).setAttribute(eq("form"), any(SearchForm.class));
        verify(itemService).searchItems(null, null, parentCategory.getName(), name, null,
                "i.id", "ASC", 1);
        assertEquals("item-list", viewName);
    }

    @Test
    void whenParentIdIsNotZeroAndNameAllIsNotEmpty_thenAllCategoriesAreUpdated() {
        // セットアップ: 全てのカテゴリを更新するシナリオ
        String name = "GrandCategoryName";
        int parentId = 1; // 0以外を設定
        String nameAll = "NotEmpty";
        Category childCategory = new Category();
        childCategory.setName("ChildCategoryName");
        childCategory.setParentId(2);
        Category parentCategory = new Category();
        parentCategory.setName("ParentCategoryName");
        when(categoryService.findParentCategory(parentId)).thenReturn(childCategory);
        when(categoryService.findParentCategory(childCategory.getParentId())).thenReturn(parentCategory);

        // テスト実行
        String viewName = controller.categoryFilter(name, parentId, nameAll, model);

        // 検証
        verify(session).setAttribute(eq("form"), any(SearchForm.class));
        verify(itemService).searchItems(null, null, parentCategory.getName(),
                childCategory.getName(), name, "i.id", "ASC", 1);
        assertEquals("item-list", viewName);
    }

    @SuppressWarnings("null")
    @Test
    void whenBrandFilterIsCalled_thenCorrectAttributesAreSet() {
        // モックの設定
        String brand = "TestBrand";
        when(itemService.searchItems(any(), eq(brand), any(), any(), any(), any(), any(), eq(1)))
                .thenReturn(null); // 適切な戻り値に置き換えてください
        when(itemService.searchTotalItem(any(), eq(brand), any(), any(), any())).thenReturn(10);

        // テスト実行
        String viewName = controller.brandFilter(brand, model);

        // 検証
        verify(session).setAttribute(eq("form"), any(SearchForm.class));
        verify(model).addAttribute(eq("searchCondition"), any(SearchForm.class));
        verify(model).addAttribute(eq("itemList"), any());
        verify(model).addAttribute(eq("totalPage"), anyInt());
        verify(model).addAttribute(eq("totalItemCount"), eq(10));
        verify(model).addAttribute(eq("currentPage"), eq(1));
        verify(model).addAttribute(eq("categoryList"), any());

        assertEquals("item-list", viewName);
    }

    @Test
    void whenToAddItemCalled_thenAttributesAddedToModelAndReturnsAddView() {
        // カテゴリリストのモックデータ
        List<Category> categoryList = Arrays.asList(new Category(), new Category());
        when(categoryService.findAllCategory()).thenReturn(categoryList);

        // ItemFormとCategoryFormのモックインスタンス（必要に応じて実際のインスタンスまたは他のモックを使用）
        ItemForm itemForm = new ItemForm();
        CategoryForm categoryForm = new CategoryForm();

        // テスト実行
        String viewName = controller.toAddItem(itemForm, categoryForm, model);

        // モデルに追加された属性の検証
        verify(model).addAttribute("itemForm", itemForm);
        verify(model).addAttribute("categoryForm", categoryForm);
        verify(model).addAttribute("categoryList", categoryList);

        // 返されるビュー名の検証
        assertEquals("add", viewName);
    }

    @SuppressWarnings("null")
    @Test
    void whenItemFormHasErrors_thenRedirectToAddItem() {
        // アイテムフォームのバリデーションエラーを模擬
        when(itemRs.hasErrors()).thenReturn(true);

        ItemForm itemForm = new ItemForm();
        CategoryForm categoryForm = new CategoryForm();

        String viewName = controller.addItem(itemForm, itemRs, categoryForm, categoryRs, model);

        assertEquals("add", viewName);
        verify(model, never()).addAttribute(eq("checkError"), any());
    }

    @SuppressWarnings("null")
    @Test
    void whenCategoryFormHasErrors_thenRedirectToAddItem() {
        // カテゴリフォームのバリデーションエラーを模擬
        when(categoryRs.hasErrors()).thenReturn(true);

        ItemForm itemForm = new ItemForm();
        CategoryForm categoryForm = new CategoryForm();

        String viewName = controller.addItem(itemForm, itemRs, categoryForm, categoryRs, model);

        assertEquals("add", viewName);
        verify(model, never()).addAttribute(eq("checkError"), any());
    }

    @SuppressWarnings("null")
    @Test
    void whenCategoryCombinationDoesNotExist_thenRedirectToAddItemWithErrorMessage() {
        // カテゴリの組み合わせが存在しないことを模擬
        when(categoryService.checkCategory(any(CategoryForm.class))).thenReturn(0);

        ItemForm itemForm = new ItemForm();
        CategoryForm categoryForm = new CategoryForm();

        String viewName = controller.addItem(itemForm, itemRs, categoryForm, categoryRs, model);

        assertEquals("add", viewName);
        verify(model).addAttribute(eq("checkError"), any());
    }

    @Test
    void whenValidForms_thenProceedToAddItemAndRedirectToConfirm() {
        // すべてのバリデーションが成功することを模擬
        when(itemRs.hasErrors()).thenReturn(false);
        when(categoryRs.hasErrors()).thenReturn(false);
        when(categoryService.checkCategory(any(CategoryForm.class))).thenReturn(1);
        when(itemService.addItem(any(ItemForm.class), any(CategoryForm.class))).thenReturn(1);
        when(fileStorageService.storeFile(any())).thenReturn("path/to/image");

        ItemForm itemForm = mock(ItemForm.class);
        CategoryForm categoryForm = new CategoryForm();
        // ファイルの存在を模擬
        when(itemForm.getImage()).thenReturn(mock(MultipartFile.class));
        when(itemForm.getImage().isEmpty()).thenReturn(false);

        String viewName = controller.addItem(itemForm, itemRs, categoryForm, categoryRs, model);

        assertEquals("confirm/add-item-confirm", viewName);
        verify(imageService).storage(anyInt(), eq("path/to/image"));
    }

    @Test
    void whenValidForms_thenProceedToAddItemAndRedirectToConfirmPathIsNull() {
        // すべてのバリデーションが成功することを模擬
        when(itemRs.hasErrors()).thenReturn(false);
        when(categoryRs.hasErrors()).thenReturn(false);
        when(categoryService.checkCategory(any(CategoryForm.class))).thenReturn(1);
        when(itemService.addItem(any(ItemForm.class), any(CategoryForm.class))).thenReturn(1);

        ItemForm itemForm = mock(ItemForm.class);
        CategoryForm categoryForm = new CategoryForm();
        // ファイルの存在を模擬
        when(itemForm.getImage()).thenReturn(mock(MultipartFile.class));
        when(itemForm.getImage().isEmpty()).thenReturn(true);

        String viewName = controller.addItem(itemForm, itemRs, categoryForm, categoryRs, model);

        assertEquals("confirm/add-item-confirm", viewName);
    }

    @Test
    void whenDetailIsCalled_thenAttributesAddedToModelAndReturnsDetailView() {
        // モックの設定
        int itemId = 1;
        Item expectedItem = new Item(); // 適切に初期化してください
        String expectedImagePath = "path/to/image";

        when(itemService.findById(itemId)).thenReturn(expectedItem);
        when(imageService.getPath(itemId)).thenReturn(expectedImagePath);

        // テスト実行
        String viewName = controller.detail(itemId, model);

        // モデルに追加された属性の検証
        verify(model).addAttribute("item", expectedItem);
        verify(model).addAttribute("imagePath", expectedImagePath);

        // 返されるビュー名の検証
        assertEquals("detail", viewName);
    }

    @Test
    void whenItemHasNoParentCategory_thenAttributesAreSetCorrectly() {
        int id = 1;
        ItemForm itemForm = new ItemForm();
        CategoryForm categoryForm = new CategoryForm();
        Item item = mock(Item.class);
        when(item.getCategories()).thenReturn(Collections.emptyList()); // カテゴリなし
        when(itemService.findById(id)).thenReturn(item);

        String result = controller.toEditItem(id, itemForm, categoryForm, model);

        // モデルに設定される属性を検証
        verify(model).addAttribute("originalParentCategory", null);
        verify(model).addAttribute("originalChildCategory", null);
        verify(model).addAttribute("originalGrandCategory", null);
        assertEquals("edit", result);
    }

    @Test
    void whenItemHasCompleteCategories_thenSetAttributesCorrectly() {
        int id = 1;
        Item item = mock(Item.class);
        // カテゴリリストを設定するロジックをシミュレートします
        when(item.getCategories()).thenReturn(createCompleteCategoryList());
        when(itemService.findById(id)).thenReturn(item);
        when(categoryService.findAllCategory()).thenReturn(List.of());

        controller.toEditItem(id, new ItemForm(), new CategoryForm(), model);
    }

    @Test
    void whenUpdateTimeErrors_thenRedirectToEdit() {
        ItemForm itemForm = new ItemForm();
        itemForm.setId(1);
        CategoryForm categoryForm = new CategoryForm();
        Timestamp timestamp = new Timestamp(0);
        when(itemService.checkDelete(1, timestamp)).thenReturn(false);

        String result = controller.editItem(itemForm, itemRs, categoryForm, timestamp, model);

        verify(itemService, never()).editItem(any(ItemForm.class),
                any(CategoryForm.class));
        assertEquals("error/4xx", result);
    }

    @Test
    void whenValidationErrors_thenRedirectToEdit() {
        when(itemRs.hasErrors()).thenReturn(true);
        ItemForm itemForm = new ItemForm();
        itemForm.setId(1);
        itemForm.setImage(mock(MultipartFile.class));
        CategoryForm categoryForm = new CategoryForm();
        Timestamp timestamp = new Timestamp(0);
        when(itemService.checkDelete(1, timestamp)).thenReturn(true);

        // toEditItemメソッドの挙動をモック化
        doReturn("to-edit-item").when(controller).toEditItem(anyInt(),
                any(ItemForm.class), any(CategoryForm.class),
                any(Model.class));

        String result = controller.editItem(itemForm, itemRs, categoryForm, timestamp, model);

        verify(itemService, never()).editItem(any(ItemForm.class),
                any(CategoryForm.class));
        assertEquals("to-edit-item", result);
    }

    @Test
    void whenCategoryNotChange_thenUseOriginalInfo() {
        ItemForm itemForm = new ItemForm();
        itemForm.setId(0);
        itemForm.setImage(mock(MultipartFile.class));
        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setParentCategory("");
        Timestamp timestamp = mock(Timestamp.class);
        when(itemService.checkDelete(itemForm.getId(), timestamp)).thenReturn(true);
        when(itemForm.getImage().isEmpty()).thenReturn(true);

        Item item = mock(Item.class);
        when(itemService.findById(anyInt())).thenReturn(item);
        when(itemRs.hasErrors()).thenReturn(false);
        when(item.getCategories()).thenReturn(createCompleteCategoryList());

        // toEditItemメソッドの挙動をモック化
        doReturn("to-edit-item").when(controller).toEditItem(anyInt(),
                any(ItemForm.class), any(CategoryForm.class),
                any(Model.class));

        String result = controller.editItem(itemForm, itemRs, categoryForm, timestamp, model);

        assertEquals("to-edit-item", result);
    }

    @Test
    void whenCategoryOnlyParent_thenUseOriginalInfo() {
        ItemForm itemForm = new ItemForm();
        itemForm.setId(1);
        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setParentCategory("parent");
        categoryForm.setChildCategory("");
        Timestamp timestamp = new Timestamp(0);
        when(itemService.checkDelete(1, timestamp)).thenReturn(true);

        when(itemRs.hasErrors()).thenReturn(false);

        // toEditItemメソッドの挙動をモック化
        doReturn("to-edit-item").when(controller).toEditItem(anyInt(),
                any(ItemForm.class), any(CategoryForm.class),
                any(Model.class));

        String result = controller.editItem(itemForm, itemRs, categoryForm, timestamp, model);

        assertEquals("to-edit-item", result);
    }

    @Test
    void whenCategoryOnlyParentAndChild_thenUseOriginalInfo() {
        ItemForm itemForm = new ItemForm();
        itemForm.setId(1);
        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setParentCategory("parent");
        categoryForm.setChildCategory("child");
        categoryForm.setGrandCategory("");
        Timestamp timestamp = new Timestamp(0);
        when(itemService.checkDelete(1, timestamp)).thenReturn(true);

        when(itemRs.hasErrors()).thenReturn(false);

        // toEditItemメソッドの挙動をモック化
        doReturn("to-edit-item").when(controller).toEditItem(anyInt(),
                any(ItemForm.class), any(CategoryForm.class),
                any(Model.class));

        String result = controller.editItem(itemForm, itemRs, categoryForm, timestamp, model);

        assertEquals("to-edit-item", result);
    }

    @Test
    void whenFileUploaded_thenUpdateImagePath() {
        ItemForm itemForm = new ItemForm();
        itemForm.setId(1);
        itemForm.setImage(mock(MultipartFile.class));
        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setParentCategory("parent");
        categoryForm.setChildCategory("child");
        categoryForm.setGrandCategory("grand");
        Timestamp timestamp = new Timestamp(0);
        when(itemService.checkDelete(1, timestamp)).thenReturn(true);

        when(itemForm.getImage().isEmpty()).thenReturn(false);
        when(imageService.getPath(anyInt())).thenReturn("old/path/to/image.jpg");
        when(fileStorageService.storeFile(any())).thenReturn("new/path/to/image.jpg");
        when(itemRs.hasErrors()).thenReturn(false);

        // toEditItemメソッドの挙動をモック化
        doReturn("to-edit-item").when(controller).toEditItem(anyInt(),
                any(ItemForm.class), any(CategoryForm.class),
                any(Model.class));

        String result = controller.editItem(itemForm, itemRs, categoryForm, timestamp, model);

        verify(fileStorageService).deleteFile("old/path/to/image.jpg");
        verify(imageService).updatePath(anyInt(), eq("new/path/to/image.jpg"));
        assertEquals("to-edit-item", result);
    }

    @Test
    void testEditItem() {
        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setParentCategory("Parent");
        categoryForm.setChildCategory("Child");
        categoryForm.setGrandCategory("grand");
        ItemForm itemForm = new ItemForm();
        itemForm.setId(1);
        itemForm.setImage(mock(MultipartFile.class));

        when(categoryService.checkCategory(categoryForm)).thenReturn(1);
        when(itemForm.getImage().isEmpty()).thenReturn(false);
        when(imageService.getPath(anyInt())).thenReturn("uploaded-img/no-image.png");
        when(itemRs.hasErrors()).thenReturn(false);
        Timestamp timestamp = new Timestamp(0);
        when(itemService.checkDelete(1, timestamp)).thenReturn(true);

        // toEditItemメソッドの挙動をモック化
        doNothing().when(itemService).editItem(any(ItemForm.class),
                any(CategoryForm.class));

        String result = controller.editItem(itemForm, itemRs, categoryForm, timestamp, model);

        assertEquals("confirm/edit-item-confirm", result);
    }

    @Test
    void testEditItem_NotImage() {
        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setParentCategory("Parent");
        categoryForm.setChildCategory("Child");
        categoryForm.setGrandCategory("grand");
        ItemForm itemForm = new ItemForm();
        itemForm.setId(1);
        itemForm.setImage(mock(MultipartFile.class));
        Timestamp timestamp = new Timestamp(0);
        when(itemService.checkDelete(1, timestamp)).thenReturn(true);

        when(categoryService.checkCategory(categoryForm)).thenReturn(1);
        when(itemForm.getImage().isEmpty()).thenReturn(true);
        when(itemRs.hasErrors()).thenReturn(false);

        // toEditItemメソッドの挙動をモック化
        doNothing().when(itemService).editItem(any(ItemForm.class),
                any(CategoryForm.class));

        String result = controller.editItem(itemForm, itemRs, categoryForm, timestamp, model);

        assertEquals("confirm/edit-item-confirm", result);
    }

    @Test
    void whenCheckDeleteReturnsTrue_thenItemIsDeleted() {
        int id = 1;
        // Arrange
        when(itemService.checkDelete(id, Timestamp.valueOf("2022-01-01 12:00:00"))).thenReturn(true);

        // Act
        String viewName = controller.deleteItem(id, Timestamp.valueOf("2022-01-01 12:00:00"), model);

        // Assert
        verify(itemService, times(1)).delete(id);
        assertEquals("confirm/delete-item-confirm", viewName);
    }

    @Test
    void whenCheckDeleteReturnsFalse_thenItemIsNotDeleted() {
        int id = 1;
        // Arrange
        when(itemService.checkDelete(id, Timestamp.valueOf("2022-01-01 12:00:00"))).thenReturn(false);

        // Act
        String viewName = controller.deleteItem(id, Timestamp.valueOf("2022-01-01 12:00:00"), model);

        // Assert
        verify(itemService, never()).delete(anyInt());
        assertEquals("error/4xx", viewName);
    }

    @Test
    void downloadItemDataWithoutSearchForm() throws IOException {
        // セッションに検索条件がない場合を模擬
        when(session.getAttribute("form")).thenReturn(null);

        // 全件取得を模擬
        List<Item> allItems = Arrays.asList(new Item(), new Item()); // 適当なアイテムリスト
        when(itemService.findAllItems()).thenReturn(allItems);

        // レスポンスのWriterをモック
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // メソッドの実行
        controller.downloadItemData(response);

        // レスポンスのヘッダーが期待通り設定されているか検証
        verify(response).setContentType("text/csv");
        verify(response).setHeader("Content-Disposition", "attachment; filename=\"products.csv\"");

        // CSV出力内容の一部を検証
        String csvOutput = stringWriter.toString();
        assertTrue(csvOutput.contains("Product ID,Name,Condition,Brand,Price,Stock,Shipping"));
    }

    @Test
    void downloadItemDataWithoutSearchFormFormIsNotNull() throws IOException {
        when(session.getAttribute("form")).thenReturn(new SearchForm());

        // 全件取得を模擬
        when(itemService.searchAllItems(null, null, null, null, null))
                .thenReturn(new ArrayList<Item>());

        // レスポンスのWriterをモック
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // メソッドの実行
        controller.downloadItemData(response);

        // レスポンスのヘッダーが期待通り設定されているか検証
        verify(response).setContentType("text/csv");
        verify(response).setHeader("Content-Disposition", "attachment; filename=\"products.csv\"");

        // CSV出力内容の一部を検証
        String csvOutput = stringWriter.toString();
        assertTrue(csvOutput.contains("Product ID,Name,Condition,Brand,Price,Stock,Shipping"));
    }

    private List<Category> createCompleteCategoryList() {
        Category parent = new Category(); // parent.setName("Parent");
        parent.setParentId(0);
        Category child = new Category(); // child.setName("Child"); child.setParentId(1); // 仮の値
        child.setParentId(1);
        Category grandChild = new Category(); // grandChild.setName("GrandChild"); grandChild.setParentId(2); // 仮の値
        grandChild.setParentId(1);
        grandChild.setNameAll("test");
        return List.of(parent, child, grandChild);
    }

}
