package com.collavore.app.approvals.service;

import java.util.Date;

import lombok.Data;

@Data
public class ApprovalstempVO {//전자결재 템플릿 테이블
		//필드
	private Integer eatNo; 	//템플릿 번호
	private String title;  	//템플릿 제목
	private String content; //템플릿 내용
	private Date regDate;	//템플릿 생선 날짜
}
//1번