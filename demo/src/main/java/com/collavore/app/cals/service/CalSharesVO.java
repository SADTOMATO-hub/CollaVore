package com.collavore.app.cals.service;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data

@Builder
public class CalSharesVO {
	private Integer csNo; //캘린더공유번호
	private Integer calNo; //캘린더번호
	private Integer empNo; //사원번호
	private Date regDate; //등록일
}
