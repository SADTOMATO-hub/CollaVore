package com.collavore.app.approvals.service;

import java.util.Date;

import lombok.Data;

@Data
public class ApproversVO {//결재자 테이블
		//필드
	private Integer earNo; 	//결재자 번호
	private Integer eaNo;  	//전자결재 번호
	private Integer empNo; 	//결재자 사원 번호
	private String status;	//결재 상태
	private Date procDate;  //결재 상태 처리일
	private Integer sort; 	//결재 상태
}
//1번