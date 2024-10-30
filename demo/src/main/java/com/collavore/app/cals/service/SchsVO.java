package com.collavore.app.cals.service;


import java.sql.Timestamp;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import groovy.transform.builder.Builder;
import lombok.Data;

@Data

@Builder
public class SchsVO {
	
	//schs
	private Integer schNo; // 일정번호  schs
	private String title; // 제목 
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
	private Timestamp   startDate; // 시작날짜 
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
	private Timestamp   endDate; // 종료날짜 
	private String isAlarm; 
	
	private Integer calNo; //캘린더 넘버 cals schs calshares
	
	
	//테이블 cals 
	private String name; // 캘린더 이름
	private String calType ; // 캘린더 타입 (개인캘린더 , 공유캘린더 , 프로젝트)
	private Date calRegDate; // 캘린더등록일
	private String isDelete; //캘린더삭제여부 (h2 미삭제 , h1 삭제 휴지통이동)
	private Date deleteDate; //캘린더삭제일 (sysdate)
	private String color;
	
	//테이블 schalarms
	private Integer saNo; // 알림번호
	private String alarmType; //알림타입 
	private String alarmYoil; //알림요일 
	private Integer alarmDay; //알림일    1~31일 숫자 <<
	private Integer alarmTime; // 알림시간  1~24 시간 <
	private Date alarmRegDate; //등록일 
	
	//테이블 calshares 
	private Integer csNo; //캘린더공유번호
	private Integer empNo; //사원번호
	private Date csRegDate; //등록일 나중에 컬럼면 변경하던지 해결해야
	
	//테이블 alarmhis
	private Integer hisNo; //일정아림내역번호
	private String alaTitle; //알림제목
	private String isView; //알림확인여부
	private Date alaRegDate; //등록일
	private Date viewDate; //알림확인일
	//private Integer saNo; //알림번호
	//private Integer empNo; //사원번호		
	
	 // 추가: employees 테이블 (사원명)
    private String empName; // 사원 이름
    
    // 추가: departments 테이블 (부서 정보)
    private Integer deptNo; // 부서 번호
    private String deptName; // 부서 이름
	
	//테이블 employees
	
}