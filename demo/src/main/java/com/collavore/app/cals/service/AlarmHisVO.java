package com.collavore.app.cals.service;

import java.util.Date;

import groovy.transform.builder.Builder;
import lombok.Data;

@Data

@Builder
public class AlarmHisVO {
	private Integer hisNo; //일정아림내역번호
	private String title; //알림제목
	private String isView; //알림확인여부
	private Date regDate; //등록일
	private Date viewDate; //알림확인일
	private Integer saNo; //알림번호
	private Integer empNo; //사원번호
}
