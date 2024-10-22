package com.collavore.app.api.service;

import lombok.Data;

@Data
public class FlutterProjVO {
	// 프로젝트 관련
	private Integer projNo;
	private String projName;
	private String projContent;
	
	private Integer mgrNo;
	
	// 프로젝트업무 관련
	private Integer pwNo;
	private String pwName;
	private String pwContent;
	
	// 프로젝트상세업무 관련
	private Integer pdwNo;
	private String pdwName;
	private String pdwContent;

}
