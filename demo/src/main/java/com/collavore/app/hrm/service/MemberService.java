package com.collavore.app.hrm.service;

import java.util.List;

public interface MemberService {

    // 로그인 인증 처리 (이메일과 비밀번호 비교)
    boolean authenticate(String email, String password); // 이메일과 비밀번호 인증

    // 사원 단건 조회
    HrmVO memberInfo(HrmVO hrmVO);

    // 사원 수정
    int memberUpdate(HrmVO hrmVO); // 사원 정보 수정 메서드

    // 이메일을 기반으로 사용자 정보 조회
    HrmVO findByEmail(String email);

    // 관리자 영역 ─────────────────────────────────────────
    // 사원 전체 조회
    List<HrmVO> selectMemberAll();

    // 사원 등록
    int insertMember(HrmVO hrmVO);

    // 사번 자동 생성 (정수형으로 변경)
    Integer generateEmpNo();

    // 사원 삭제 (사번을 정수형으로 처리)
    int deleteMember(int empNo); 
}
