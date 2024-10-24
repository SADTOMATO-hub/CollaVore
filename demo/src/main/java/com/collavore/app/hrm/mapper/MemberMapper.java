package com.collavore.app.hrm.mapper;

import java.util.List;
import com.collavore.app.hrm.service.HrmVO;

public interface MemberMapper {

	// 이메일을 통해 사용자 정보 조회
	HrmVO findByEmail(String email);

	// 사원 단건 조회
	HrmVO selectMemberInfo(HrmVO hrmVO);

	// 사원 정보 수정
	int updateMember(HrmVO hrmVO);

	// 사원 전체 조회 (관리자)
	List<HrmVO> selectMemberAll();

	// 사원 등록 (관리자)
	int insertMember(HrmVO hrmVO);
	
	// 연락처 중복 확인
    int checkTelDuplicate(String tel);

    // 이메일 중복 확인
    int checkEmailDuplicate(String email);
    
	// 사번으로 단건 조회
	HrmVO selectMemberById(Integer empNo);

	// 사원 정보 수정(관리자)
	int updateMemberByAdmin(HrmVO hrmVO);
	
	 // 사원 정보 조회
	HrmVO getMemberById(Integer empNo);
	
	// 사원 삭제 (관리자)
	int deleteMember(Integer empNo); // 사번을 정수형으로 처리

	// 오늘 날짜로 시작하는 가장 큰 사번을 조회 (정수형으로 반환)
	Integer getMaxEmpNo(String today); // 가장 큰 사번을 정수형으로 반환
}
