package com.collavore.app.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class HomeVO {
	// 게시판관련
	private Integer boardNo;
	private String name;
	private Integer postNo;
	private String empName;
	private String title;
	private String content;
	private String profileImg;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date regDate;
	   
	 // 전자결재관련
	private Integer eaNo;
	private String appTitle;
	private String appStatus;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date appDate;
}
