package com.collavore.app.cals.service;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data

@Builder
public class CalsVO {
	private Integer calNo;
	private String name;
	private Integer type ;
	private Date regDate;
	private String isDelete;
	private Date deleteDate;
	
}
