package com.collavore.app.hrm.service;

import java.util.List;

public interface DeptService {

	// 새로운 부서 추가
	public int insertDept(HrmVO hrmVO) throws Exception;

	// 기존 부서 정보 업데이트
	public int updateDept(HrmVO hrmVO) throws Exception;

	// 부서 삭제
	public int deleteDept(Integer deptNo) throws Exception;

	// 부서에 사원이 있는지 확인하는 메서드 추가
	boolean hasEmployeesInDept(Integer deptNo);

	// 기존 부서 목록 가져오기
	public List<HrmVO> getExistingDepts();
	
	// 부서별 사원 목록 조회
    List<HrmVO> getEmployeesByDept(Integer deptNo);
    
    // 부서장 업데이트
    int updateManager(Integer deptNo, Integer empNo);

}
