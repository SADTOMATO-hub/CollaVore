package com.collavore.app.api.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
	private String info;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date joinDate;
	private String deptName;
	private String jobName;
	private String posiName;
	private String profileImg;
	
}
