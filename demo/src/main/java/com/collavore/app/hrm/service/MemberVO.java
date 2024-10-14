package com.collavore.app.hrm.service;

import java.util.Date;

import lombok.Data;

@Data
public class MemberVO {
	private Integer empNo;   // 사원번호
	private String email;    // 이메일
	private String password; // 비밀번호
	private String empName;  // 사원이름
	private String tel;      // 전화번호
	private String address;  // 주소
	private String workType; // 근무형태
	private String info;     // 나의 소개글
	private Date joinDate;   // 입사일
	private Date leaveDate;  // 퇴사일
	private Date regDate;    // 등록일
	private Integer deptNo;  // 부서번호
	private Integer jobNo;   // 직무번호
	private Integer posiNo;  // 직위번호
	private String gitToken; // 개별토큰
}
