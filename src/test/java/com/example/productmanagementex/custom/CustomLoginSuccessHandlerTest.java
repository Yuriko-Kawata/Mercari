package com.example.productmanagementex.custom;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLoginSuccessHandlerTest {
    @InjectMocks
    private CustomLoginSuccessHandler successHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void onAuthenticationSuccess_redirectsToItemList() throws IOException, ServletException {
        // モックの設定
        when(authentication.getName()).thenReturn("user");

        // メソッドのテスト実行
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // リダイレクトが期待通りに行われたか検証
        verify(response).sendRedirect("itemList");

        // ログ出力が期待通りに行われたかも検証したい場合は、Loggerをモック化して検証する必要があります。
        // これには、Loggerのモック化に特化したツールやアプローチを使用することが推奨されます。
    }
}
