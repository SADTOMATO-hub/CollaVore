package com.collavore.app.project.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ProjectFilesVO {

	// 프로젝트 파일관리
	private Integer projFileNo;
	private Integer pfNo;
	private String name;
	private String content;
	//private String fileSize;
	private String extension;
	private String filePath;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date regDate;
    private long fileSize;
	
	

}
