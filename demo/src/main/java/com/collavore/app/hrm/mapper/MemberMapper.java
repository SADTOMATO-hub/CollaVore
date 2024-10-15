package com.collavore.app.hrm.mapper;

import java.util.List;

import com.collavore.app.hrm.service.HrmVO;

public interface MemberMapper {
   // 사원 전체조회(관리자)
   public List<HrmVO> selectMemberAll();
   
   // 사원 단건조회
   public HrmVO selectMemberInfo(HrmVO hrmVO);
   
   // 사원 등록
   public int insertMember(HrmVO hrmVO);
   
   // 사원 수정
   
   
   // 사원 삭제
}
