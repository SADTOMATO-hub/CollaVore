package com.collavore.app.cals.service;

import java.util.Date;

import groovy.transform.builder.Builder;
import lombok.Data;

@Data

@Builder
public class SchsVO {
	private Integer schNo;  // 자동 생성되는 번호
    private String title;   // 일정 제목
    private Date startDate; // 시작 날짜
    private Date endDate;   // 종료 날짜
    private Integer calNo;  // 캘린더 번호
    private String isAlarm; // 알림 여부 (예/아니오)
}
