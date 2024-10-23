package com.collavore.app.security.service;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserVO {
    private String email;
    private String password;
    private Integer empNo;
    private List<String> roleNames = new ArrayList<>(); // 역할 목록을 List로 변경 및 초기화
}
