package com.collavore.app.cals.service;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data

@Builder
public class CalSharesVO {
	private Integer csNo;
	private Integer calNo;
	private Integer empNo;
	private Date regDate;
}
