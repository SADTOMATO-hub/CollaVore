package com.collavore.app.project.service;

import java.util.Date;

import lombok.Data;

@Data
public class ProjectDWorkComtVO {

	// 프로젝트 상세 업무 코멘트관리
	private Integer pdwcNo;
	private Integer wrtierNo;
	private Integer pdwNo;
	private String content;
	private Date regDate;

}
