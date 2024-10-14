package com.collavore.app.cals.service;

import java.util.Date;

import groovy.transform.builder.Builder;
import lombok.Data;

@Data

@Builder
public class AlarmHisVO {
	private Integer hisNo;
	private String title;
	private String isView;
	private Date regDate;
	private Date viewDate;
	private Integer saNo;
	private Integer empNo;
}
