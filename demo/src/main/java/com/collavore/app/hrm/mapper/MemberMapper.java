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

	
	
	// 사원 전체 조회 (관리자)
	List<HrmVO> selectMemberAll(String page);
	
	// 필터링된 사원 목록 조회
	List<HrmVO> selectFilteredMembers(@Param("offset") int offset, 
            @Param("deptFilter") String deptFilter, 
            @Param("jobFilter") String jobFilter, 
            @Param("posiFilter") String posiFilter, 
            @Param("workTypeFilter") String workTypeFilter,
            @Param("searchText") String searchText);
	
	// 필터링된 총 사원 수 조회
	int getTotalListCnt(@Param("deptFilter") String deptFilter, 
            @Param("jobFilter") String jobFilter, 
            @Param("posiFilter") String posiFilter, 
            @Param("workTypeFilter") String workTypeFilter,
            @Param("searchText") String searchText);
	
	// 부서 목록 조회
    List<String> getDepartments();

    // 직무 목록 조회
    List<String> getJobs();

    // 직위 목록 조회
    List<String> getPositions();
    
    // 상태 목록 조회
    List<String> getWorkType();

    
    
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
	
	
	// 모든 사원 목록 조회
    List<HrmVO> selectAllMembers();

    // 부서 및 직위 정보를 포함한 사원 목록 조회
    List<HrmVO> selectMembersWithDeptAndPosition();

 // 나의 전자결재
	List<HrmVO> selectAppList(Integer empNo);
}
