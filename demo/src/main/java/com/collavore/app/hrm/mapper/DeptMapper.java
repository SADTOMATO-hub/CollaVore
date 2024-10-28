package com.collavore.app.hrm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.collavore.app.hrm.service.HrmVO;

public interface DeptMapper {

	// 부서 정보 삽입
	public int insertDept(HrmVO hrmVO);

	// 부서 정보 업데이트
	public int updateDept(HrmVO hrmVO);

	// 부서 삭제
	public int deleteDept(@Param("deptNo") Integer deptNo);

	// 부서에 속한 사원 수를 확인하는 메서드
	int countEmployeesInDept(@Param("deptNo") Integer deptNo);

	// 기존 부서 목록 조회
	public List<HrmVO> selectDeptList();

	// 부서별 사원 목록 조회
	List<HrmVO> selectEmployeesByDept(@Param("deptNo") Integer deptNo);

	// 부서장 업데이트
	int updateManager(@Param("deptNo") Integer deptNo, @Param("empNo") Integer empNo);
}