package com.collavore.app.api.service;

import lombok.Data;

@Data
public class FlutterVO {
	// 회원관련
	private String email; // 아이디
	private String password; // 비밀번호
	private String name; // 이름
	private String tel; // 연락처

}
