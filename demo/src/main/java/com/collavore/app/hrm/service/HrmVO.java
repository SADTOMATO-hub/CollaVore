package com.collavore.app.hrm.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class HrmVO {
   private Integer empNo;        // 사원번호
   private String email;         // 이메일
   private String password;      // 비밀번호
   private String empName;       // 사원이름
   private String tel;           // 전화번호
   private String address;       // 주소
   private String workType;      // 근무형태
   private String info;          // 나의 소개글
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private Date joinDate;        // 입사일
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private Date leaveDate;       // 퇴사일
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private Date regDate;         // 등록일
   private String gitToken;      // 개별토큰
   private String img;           // 이미지
   
   private Integer deptNo;       // 부서번호
   private String deptName;      // 부서명
   private Integer mgrNo;        // 부서장 사원번호
   private Integer parentDeptNo; // 상위부서번호
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private Date deptDate;        // 부서 등록일
   private Integer empCnt;       // 사원수
   
   private Integer jobNo;        // 직무번호
   private String jobName;       // 직무명
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private Date jobDate;         // 직무 등록일
   
   private Integer posiNo;       // 직위번호
   private String posiName;      // 직위명
   private Integer grade;        // 직위등급
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private Date posiDate;        // 직위 등록일
   
   private String page;			 // 페이지
   
}
