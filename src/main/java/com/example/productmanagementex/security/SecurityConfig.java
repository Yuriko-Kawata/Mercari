package com.example.productmanagementex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.productmanagementex.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SHA256PasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/login", "/toRegister", "register").permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/itemList", true)
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
