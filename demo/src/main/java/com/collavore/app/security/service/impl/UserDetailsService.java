package com.collavore.app.security.service.impl;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.collavore.app.security.mapper.UserMapper;
import com.collavore.app.security.service.LoginUserVO;
import com.collavore.app.security.service.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean authenticate(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserVO userVO = userMapper.findByEmail(email);
        if (userVO == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new LoginUserVO(userVO);
    }

    public UserVO findByEmail(String email) {
        return userMapper.findByEmail(email);
    }
    
    public List<String> myMenuAuth(int empNo) {
    	return userMapper.myMenuAuth(empNo);
    }
}
