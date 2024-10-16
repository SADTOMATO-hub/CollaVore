package com.collavore.app.project.service;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ProjectVO {
	
	//프로젝트관리
	private Integer projNo;
	private String name;
	private String content;	
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
	private String isTemplate;	
	private String status;	
	private Integer projTempNo;
	private String projectGitUrl;
	private Integer pMgrNo;

	
	
	
}
