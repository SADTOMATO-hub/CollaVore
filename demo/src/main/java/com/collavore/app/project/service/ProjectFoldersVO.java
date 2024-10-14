package com.collavore.app.project.service;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectFoldersVO {
	
	//프로젝트 폴더관리
	private Integer pfNo;
	private String name;	
	private Integer pwNo;
	private Date regDate;
	
	
	
	
}
