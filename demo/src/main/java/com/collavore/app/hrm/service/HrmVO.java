package com.collavore.app.hrm.service;

import java.util.Date;

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
   private Date joinDate;        // 입사일
   private Date leaveDate;       // 퇴사일
   private Date regDate;         // 등록일
   private String gitToken;      // 개별토큰
   
   private Integer deptNo;       // 부서번호
   private String deptName;      // 부서명
   private Integer mgrNo;        // 부서장 사원번호
   private Integer parentDeptNo; // 상위부서번호
   private Date deptDate;        // 부서 등록일
   private Integer empCnt;       // 사원수
   
   private Integer jobNo;        // 직무번호
   private String jobName;       // 직무명
   private Date jobDate;         // 직무 등록일
   
   private Integer posiNo;       // 직위번호
   private String posiName;      // 직위명
   private Integer grade;        // 직위등급
   private Date posiDate;        // 직위 등록일
   
}
