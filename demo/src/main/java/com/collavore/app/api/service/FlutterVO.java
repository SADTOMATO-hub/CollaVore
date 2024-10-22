package com.collavore.app.api.service;

import lombok.Data;

@Data
public class FlutterVO {
	// 회원관련
	private String email; // 이메일아이디
	private String password; // 비밀번호
	private String resetPwd; // 변경할 비밀번호
	private String name; // 이름
	private String tel; // 연락처
	private Integer empNo; //사원번호
}
