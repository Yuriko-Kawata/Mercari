package com.example.productmanagementex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.productmanagementex.custom.CustomLoginSuccessHandler;
import com.example.productmanagementex.custom.CustomUserDetailsService;

/**
 * Spring Security設定を提供
 * ユーザー認証とHTTPリクエストの許可を管理
 * カスタムユーザー詳細サービスを使用してユーザー詳細を取得
 * SHA-256パスワードエンコーダーを使用してパスワードをエンコードする
 * 
 * @author hiraizumi
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * SHA-256パスワードエンコーダーを提供
     *
     * @return SHA-256パスワードエンコーダー
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SHA256PasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

    /**
     * HTTPリクエストのフィルタチェーンを提供
     * 特定のURLへのアクセスを許可し、他の全てのリクエストを認証
     * ログインページ、デフォルトの成功URL、ログアウト処理、ユーザー詳細サービスを設定
     *
     * @param http HttpSecurityオブジェクト
     * @return セキュリティフィルタチェーン
     * @throws Exception 例外
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/login", "/toRegister", "register").permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout") // ログアウト処理を行うURL
                        .logoutSuccessUrl("/login") // ログアウト成功後にリダイレクトされるURL
                        .invalidateHttpSession(true) // セッションを無効化
                        .clearAuthentication(true) // 認証情報をクリア
                        .deleteCookies("JSESSIONID") // セッションクッキーを削除
                        .permitAll())
                .userDetailsService(customUserDetailsService);

        return http.build();
    }

}
