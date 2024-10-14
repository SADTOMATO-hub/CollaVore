package com.collavore.app.project.service;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectVO {
	
	//프로젝트관리
	private Integer projNo;
	private String name;
	private String content;
	private Date regDate;

	
	
	
}
