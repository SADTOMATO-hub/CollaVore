package com.collavore.app.approvals.service;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ApprovalsVO {//전자결재 테이블
		//전자결재 테이블 필드
	private Integer eaNo; 	//전자결재 번호
	private String title; 	//전자결재 제목
	private Integer eatNo;	//템플릿 번호
	private String content; //전자결재 내용
	private String ApprovalStatus;	//전자결재 상태
	private Integer drafterEmpNo;  //기안자 사원번호 empNo
	private Date regDate; 	//기안 날짜
	private Date compDate; 	//전자결재 완료 날짜
	private Integer earNo;    //조회용, 결재자 사번
		//결재자 테이블 필드
	//private Integer earNo; 	//결재자 번호
	private List<Integer> empNoList; 	//입력용,결재자 사번
	//private Integer eaNo;  	//전자결재 번호
	private List<String> status;	//결재 상태
	private List<Date> procDate;  //결재 상태 처리일
	private List<Integer> sort; 	//결재 순서
	
	
}
//1번