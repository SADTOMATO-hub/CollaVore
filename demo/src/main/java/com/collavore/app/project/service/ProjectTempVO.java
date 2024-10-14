package com.collavore.app.project.service;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectTempVO {
	
	//프로젝트 템플릿관리
	private Integer projTempNo;
	private String name;
	private String content;
	private Date periodDate;

	
	
	
}
