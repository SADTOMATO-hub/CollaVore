package com.collavore.app.project.service;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDWorkTempVO {
	
	//프로젝트 상세 업무 템플릿 관리
	private Integer pdwtNo;
	private String name;
	private String content;
	private Integer pwtNo;
	private String importance;
	private Date regDate;

	
	
	
}
