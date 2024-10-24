package com.collavore.app.security.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.collavore.app.security.service.UserVO;

@Mapper
public interface UserMapper {
    public UserVO findByEmail(String email);
    public List<String> findRolesByEmail(String email); // 역할 조회 메서드 추가
}
