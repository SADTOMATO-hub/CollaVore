package com.collavore.app.api.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class FlutterApprVO {
	private Integer empNo; // 로그인한 사원번호
	
	// 전자결재문서관련
	private Integer eaNo; // 전자결재번호
	private String title; // 전자결재명
	private String content; // 전자결재내용(HTML 태그가 들어가있음)
	private String status; // 전자결재상태여부
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date regDate; // 전자결재기안날짜
	private Integer appEmpNo; // 전자결재기안자
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date compDate; // 전자결재완료날짜
	
	// 전자결재결재자관련
	private Integer earNo; // 전자결재자번호
	private Integer apprEmpNo; // 전자결재자사원번호
	private String apprstatus; // 전자결재자 결재상태
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date procDate; // 전자결재자 결재처리날짜
	private Integer sort; // 전자결재자 정렬순서
	
}
