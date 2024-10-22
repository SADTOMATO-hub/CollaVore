package com.collavore.app.cals.service;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data

@Builder
public class CalsVO {
	private Integer calNo; //캘린더 번호
	private String name; // 캘린더 이름
	private String type ; // 캘린더 타입 (개인캘린더 , 공유캘린더 , 프로젝트)
	private Date regDate; // 캘린더등록일
	private String isDelete; //캘린더삭제여부 (h2 미삭제 , h1 삭제 휴지통이동)
	private Date deleteDate; //캘린더삭제일 (sysdate)
	private String color;
	
	
	//캘린더 삭제누르면 휴지통으로 가게하기
	//풀캘린더 안에 단건조회창 안에서 누르면 즉시 삭제
}
