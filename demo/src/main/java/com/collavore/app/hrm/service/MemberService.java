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
    // public int memberUpdate(HrmVO hrmVO);

    // 사원 삭제
    // public int memberDelete(int empNo);

    // 이메일을 기반으로 사용자 정보 조회
    public HrmVO findByEmail(String email);
    

    // 로그인 인증 처리 (이메일과 비밀번호 비교)
    public boolean authenticate(String email, String password);  // 이 부분 추가
}
