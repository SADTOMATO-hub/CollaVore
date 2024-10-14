package com.collavore.app.cals.service;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data

@Builder
public class SchAlarmsVO {
	private Integer saNo;
	private Integer schNo;
	private String repAlarmType;
	private Integer repAlarmCnt;
	private String isRep;
	private Integer repCnt;
	private Date repEndDate;
}
