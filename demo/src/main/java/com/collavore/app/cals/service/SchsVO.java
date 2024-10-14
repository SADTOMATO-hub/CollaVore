package com.collavore.app.cals.service;

import java.util.Date;

import groovy.transform.builder.Builder;
import lombok.Data;

@Data

@Builder
public class SchsVO {
	private Integer schNo;
	private String title;
	private Date startDate;
	private Date endDate;
	private Integer calNo;
	private String isAlarm;
	
}
