package com.collavore.app.approvals.service;

import java.util.Date;

import lombok.Data;

@Data
public class ApprovalsVOBackup {//전자결재 테이블
		//필드
	private Integer eaNo; 	//전자결재 번호
	private String title; 	//전자결재 제목
	private Integer eatNo;	//템플릿 번호
	private String content; //전자결재 내용
	private String Status;	//전자결재 상태
	private Integer EmpNo;  //기안자 사원번호
	private Date regDate; 	//기안 날짜
	private Date compDate; 	//전자결재 완료 날짜
	private Integer earNo; 	//결재자 번호
}
//1번