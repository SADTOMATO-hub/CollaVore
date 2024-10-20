package com.collavore.app.project.service;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
public class ProjectFilesVO {

	// 프로젝트 파일관리
	private Integer projFileNo;
	private Integer pfNo;
	private String name;
	private String content;
	private String size;
	private String extension;
	private String path;
	private Date regDate;

}
