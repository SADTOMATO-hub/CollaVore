package com.collavore.app.hrm.service;

import java.util.List;

public interface MemberService {
   // 사원 전체조회(관리자)
   public List<HrmVO> memberList();
   
   // 사원 단건조회
   public HrmVO memberInfo(HrmVO hrmVO);
   
   // 사원 등록
   public int memberInsert(HrmVO hrmVO);
   
   // 사원 수정
   
   
   // 사원 삭제
}
