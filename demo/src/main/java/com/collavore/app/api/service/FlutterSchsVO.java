package com.collavore.app.api.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class FlutterSchsVO {
	private Integer schNo; // 일정번호
	private String schTitle; // 일정명
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate; // 일정시작일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate; // 일정종료일
	private String calName; // 캘린더명
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date selectDate; // 선택날짜
	private Integer empNo; // 로그인한사원번호
	private Integer calNo; // 캘린더번호
	
}
