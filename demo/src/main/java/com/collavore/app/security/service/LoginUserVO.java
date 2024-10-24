package com.collavore.app.security.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginUserVO implements UserDetails {
    private final UserVO userVO;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 역할 목록을 가져와 GrantedAuthority 리스트로 변환
        return userVO.getRoleNames().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userVO.getPassword();
    }

    @Override
    public String getUsername() {
        return userVO.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserVO getUserVO() {
        return userVO; // getUserVO 메서드 추가
    }
}
