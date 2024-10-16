package com.collavore.app.approvals.service;

import java.util.Date;

import lombok.Data;

@Data
public class ApprovalsVO {//전자결재 테이블
		//필드
	private Integer eaNo; 	//전자결재 번호
	private String Title; 	//전자결재 제목
	private Integer eatNo;	//템플릿 번호
	private String content; //전자결재 내용
	private String status;	//전자결재 상태
	private Integer empNo;  //기안자 사원번호
	private Date regDate; 	//기안 날짜
	private Date compDate; 	//전자결재 완료 날짜
}
//1번