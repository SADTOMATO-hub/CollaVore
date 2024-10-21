package com.collavore.app.cals.service;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data

@Builder
public class SchAlarmsVO {
	private Integer saNo; // 알림번호
	private Integer schNo; // 일정번호
	private String repAlarmType; //반복빈도타입
	private Integer repAlarmCnt; //반복빈도수
	private String isRep; //계속반복여부
	private Integer repCnt; // 계속반복횟수
	private Date repEndDate; //계속반복종료일
}
