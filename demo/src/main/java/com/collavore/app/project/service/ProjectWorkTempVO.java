package com.collavore.app.project.service;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ProjectWorkTempVO {
	
	//프로젝트 업무 템플릿 관리
	private Integer pwtNo;
	private String name;
	private String content;
	private Integer projTempNo;
	private Integer jobNo;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date regDate;

	
	
	
}
