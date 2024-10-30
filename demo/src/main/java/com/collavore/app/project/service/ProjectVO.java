package com.collavore.app.project.service;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ProjectVO {
	
	//프로젝트관리
	private Integer projNo;
	private String name;
	private String content;	
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
	private String isTemplate;	
	private String status;	
	private Integer projTempNo;
	private String projectGitUrl;
	private Integer pMgrNo;
//	private String pMgrNo;
	
	// 프로젝트 업무관리
	private Integer pwNo;
	private String workType;
	private Integer mgrNo;
	private Integer sort;
	private Integer jobNo;
	
	//프로젝트 상세 업무 관리
	private Integer pdwNo;
	private Integer parentPdwNo;
	private Date regDate;
	private String isStatus;
    private Date reqDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date compDate;
    private String commitId;
    private String importance;
	
	private String parentNo;
	private String no;
	private String selPwNo;
	private String selParentPdwNo;
	private Integer level ;
	
	// 프로젝트 상세 업무 코멘트관리
	private Integer pdwcNo;
	private Integer writerNo;

	// 부서정보
	private Integer deptNo;
	
	// 사원정보
	private Integer empNo;
	private String empName;
	
	
	private String jobName;
	private String table;
	
	private Integer pwtNo;
	
}
