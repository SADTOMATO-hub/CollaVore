package com.collavore.app.project.service;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDWorkComtVO {
	
	//프로젝트 상세 업무 코멘트관리
	private Integer pdwcNo;
	private Integer wrtierNo;
	private Integer pdwNo;
	private String content;	
	private Date regDate;

	
	
	
}
