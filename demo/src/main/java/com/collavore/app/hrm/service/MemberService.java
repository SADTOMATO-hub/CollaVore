package com.collavore.app.hrm.service;

import java.util.List;

public interface MemberService {
	// 게시글 전체 건수 확인하기(페이징을 위함)
	public int totalListCnt();

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
	List<HrmVO> selectMemberFiltered(String deptNo, String jobNo, String posiNo, String workType, String page);

	// 사원 등록
	int insertMember(HrmVO hrmVO);

	/// 연락처 중복 확인 메서드
    boolean isTelDuplicate(String tel);

    // 이메일 중복 확인 메서드
    boolean isEmailDuplicate(String email);

	// 사번 자동 생성 (정수형으로 변경)
	Integer generateEmpNo();

	// 사원 단건 조회
	HrmVO memberInfoByEmpNo(Integer empNo);

	// 사원 정보 수정
	int updateMemberByAdmin(HrmVO hrmVO);

	// 사원 정보 조회
	HrmVO getMemberById(Integer empNo);

	// 사원 삭제 (사번을 정수형으로 처리)
	int deleteMember(Integer empNo);

}
