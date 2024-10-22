package com.collavore.app.project.service;


import java.util.Date;

import lombok.Data;

@Data
public class ProjectDWorkVO {
	
	//프로젝트 상세 업무 관리
	private Integer pdwcNo;
	private String content;
	private Integer writerNo;
	private Integer pdwNo;
	private Date regDate;

	
	
	
}
