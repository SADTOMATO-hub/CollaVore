package com.collavore.app.project.service;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDWorkVO {
	
	//프로젝트 상세 업무 관리
	private Integer pdwcNo;
	private String content;
	private Integer writerNo;
	private Integer pdwNo;
	private Date regDate;

	
	
	
}
