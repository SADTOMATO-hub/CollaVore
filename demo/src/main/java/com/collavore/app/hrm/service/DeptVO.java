package com.collavore.app.hrm.service;

import java.util.Date;

import lombok.Data;

@Data
public class DeptVO {
	private Integer daptNO;       // 부서번호
	private String deptName;      // 부서명
	private Integer mgrNo;        // 부서장 사원번호
	private Integer parentdeptNo; // 상위부서번호
	private Integer sort;         // 정렬순서
	private Date deptDate;        // 부서 등록일
	private String jobNo;		  // 직무번호
	private Integer jobName;      // 직무명
	private Date jobDate;         // 직무 등록일
	private Integer posiNo;       // 직위번호
	private String posiname;      // 직위명
	private Integer grade;        // 직위등급
	private Date posiDate;        // 직위 등록일
}
