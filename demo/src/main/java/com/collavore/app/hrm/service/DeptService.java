package com.collavore.app.hrm.service;

import java.util.List;

public interface DeptService {

	// 사원-부서리스트
	List<HrmVO> getOrganizationStructure(Integer deptNo, String isMgr);
	
	// 부서 등록
	public int insertDept(HrmVO hrmVO) throws Exception;

	// 부서 수정
	public int updateDept(HrmVO hrmVO) throws Exception;

	// 부서 삭제
	public int deleteDept(Integer deptNo) throws Exception;

	// 부서에 사원이 있는지 확인하여 삭제 불가 메시지 반환
	boolean hasEmployeesInDept(Integer deptNo);

	// 조직도 데이터를 계층 구조로 반환 (기존부서목록조회)
	public List<HrmVO> getExistingDepts();
	
	// 부서별 사원 목록 조회
    List<HrmVO> getEmployeesByDept(Integer deptNo);
    
	// 부서장 업데이트
    int updateManager(Integer deptNo, Integer empNo);
    
    // 부서정보 관련
    // 부서부서장조회
    public List<HrmVO> getMgrList();
    
    // 부서사원조회
    public List<HrmVO> getMemberList(Integer deptNo);



}
