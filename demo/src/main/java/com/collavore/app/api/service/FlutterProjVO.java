package com.collavore.app.api.service;

import lombok.Data;

@Data
public class FlutterProjVO {
	// 프로젝트 관련
	private Integer projNo; // 프로젝트번호
	private String projName; // 프로젝트명
	private String projContent; // 프로젝트개요
	
	private Integer mgrNo; // 프로젝트담당자
	
	// 프로젝트업무 관련
	private Integer pwNo; // 프로젝트업무번호
	private String pwName; // 프로젝트업무명
	private String pwContent; // 프로젝트업무설명
	
	// 프로젝트상세업무 관련
	private Integer pdwNo; // 프로젝트상세업무번호
	private String pdwName; // 프로젝트상세업무명
	private String pdwContent; // 프로젝트상세업무설명

}
