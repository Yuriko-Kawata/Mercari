package com.example.productmanagementex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.productmanagementex.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // あなたのユーザーリポジトリ

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        com.example.productmanagementex.domain.User user = userRepository.findUserByMail(mail);
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + mail);
        }
        return User.builder()
                .username(user.getMail())
                .password(user.getPassword()) // データベースに保存されているハッシュ化されたパスワード
                .roles("USER")
                .build();
    }
}
