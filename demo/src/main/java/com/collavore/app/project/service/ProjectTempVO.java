package com.collavore.app.project.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ProjectTempVO {

	// 프로젝트 템플릿관리
	private Integer projTempNo;
	private String name;
	private String content;
	private Integer periodDate;

	
	//프로젝트 업무 템플릿 관리
	private Integer pwtNo;
	private Integer jobNo;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date regDate;
	
	
	//프로젝트 상세 업무 템플릿 관리
	private Integer pdwtNo;
	private String importance;
}
