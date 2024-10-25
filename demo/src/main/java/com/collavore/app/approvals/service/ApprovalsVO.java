package com.collavore.app.approvals.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ApprovalsVO {//전자결재 테이블
		//전자결재 테이블 필드
	private Integer eaNo; 	//전자결재 번호
	private String title; 	//템플릿 제목
	private Integer eatNo;	//템플릿 번호
	private String content; //전자결재 내용
	private String approvalStatus;	//전자결재 상태
	private Integer drafterEmpNo;  //기안자 사원번호 empNo
	private Date regDate; 	//기안 날짜
	private Date compDate; 	//전자결재 완료 날짜
	private Integer earNo;    //조회용, 결재자 사번
		//결재자 테이블 필드
	List<ApprovalsVO> approvers = new ArrayList<>();
	//private Integer earNo; 	//결재자 번호
	//private List<Integer> empNoList; 	//입력용,결재자 사번
	//private Integer eaNo;  	//전자결재 번호
	//private List<Integer> sortList; 	//결재 순서
	private Integer empNo;	//결재자 사번
	private Integer sort;		//결재 순서
	private String status;	//결재자의 결재 상태
	private Date procDate;  //결재 상태 처리일
	
	
	//JOIN용 컬럼알리아스
	private String approverName;
	private String approverEmpNo;
	private String jobTitle;
	private String drafterName;
	private String approverStatus;
	
}
//1번