package com.collavore.app.hrm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collavore.app.hrm.mapper.DeptMapper;
import com.collavore.app.hrm.service.DeptService;
import com.collavore.app.hrm.service.HrmVO;

@Service
public class DeptServiceImpl implements DeptService {

	private final DeptMapper deptMapper;

	@Autowired
	public DeptServiceImpl(DeptMapper deptMapper) {
		this.deptMapper = deptMapper;
	}

	@Override
	public List<HrmVO> getOrganizationStructure(Integer deptNo, String isMgr) {
		// 부서 및 사원 정보를 특정 조건으로 조회
		return deptMapper.getOrganizationStructure(deptNo, isMgr);
	}

	@Override
	public int insertDept(HrmVO hrmVO) throws Exception {
		// 새로운 부서 추가
		return deptMapper.insertDept(hrmVO);
	}

	@Override
	public int updateDept(HrmVO hrmVO) throws Exception {
		// 기존 부서 정보 업데이트
		return deptMapper.updateDept(hrmVO);
	}

	@Override
	public int deleteDept(Integer deptNo) throws Exception {
		// 부서 삭제
		return deptMapper.deleteDept(deptNo);
	}

	@Override
	public boolean hasEmployeesInDept(Integer deptNo) {
		// 부서에 사원이 있는지 확인
		return deptMapper.countEmployeesInDept(deptNo) > 0;
	}

	@Override
	public List<HrmVO> getExistingDepts() {
		// 기존 부서 목록 가져오기
		return deptMapper.selectDeptList();
	}

	@Override
	public List<HrmVO> getEmployeesByDept(Integer deptNo) {
		// 특정 부서별 사원 목록 조회
		return deptMapper.selectEmployeesByDept(deptNo);
	}
	
	@Override
	public HrmVO getMgrByDept(Integer deptNo) {
		// 부서 조직장 조회
		return deptMapper.selectMgrByDept(deptNo);
	}

	@Override
	public int updateManager(Integer deptNo, Integer empNo) {
		// 부서장 업데이트
		return deptMapper.updateManager(deptNo, empNo);
	}
	
	// 부서정보관련 
	// 조회
	@Override
	public List<HrmVO> getMgrList() {
		return deptMapper.getMgrList();
	}

	@Override
	public List<HrmVO> getMemberList(Integer deptNo) {
		return deptMapper.getMemberList(deptNo);
	}
}
