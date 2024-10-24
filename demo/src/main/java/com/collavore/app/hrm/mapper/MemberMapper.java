package com.collavore.app.hrm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.collavore.app.hrm.service.HrmVO;

public interface MemberMapper {
	// 전체 사원 수 가져오기
	int totalCnt();

	// 이메일을 통해 사용자 정보 조회
	HrmVO findByEmail(String email);

	// 사원 단건 조회
	HrmVO selectMemberInfo(HrmVO hrmVO);

	// 사원 정보 수정
	int updateMember(HrmVO hrmVO);

	// 관리자 영역
	// 사원 전체 조회 (관리자)
    List<HrmVO> selectMemberAll(@Param("pageStart") int pageStart, @Param("pageSize") int pageSize);

    List<HrmVO> selectMemberFiltered(
            @Param("deptNo") String deptNo,
            @Param("jobNo") String jobNo,
            @Param("posiNo") String posiNo,
            @Param("workType") String workType,
            @Param("pageStart") int pageStart,
            @Param("pageSize") int pageSize
        );
    
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
