package com.collavore.app.hrm.mapper;

import java.util.List;
import com.collavore.app.hrm.service.HrmVO;

public interface MemberMapper {

    // 사원 전체 조회 (관리자용)
    List<HrmVO> selectMemberAll();
   
    // 사원 단건 조회 (사원 번호로 조회)
    HrmVO selectMemberInfo(HrmVO hrmVO);
   
    // 사원 등록 (insert)
    int insertMember(HrmVO hrmVO);
   
    // 사원 정보 수정
    int updateMember(HrmVO hrmVO);
   
    // 사원 삭제 (사원 번호로 삭제)
    int deleteMember(int empNo);

    // 이메일을 통해 사용자 정보 조회
    HrmVO findByEmail(String email);
}