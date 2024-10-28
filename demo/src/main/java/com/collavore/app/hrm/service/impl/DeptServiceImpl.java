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

    // 트랜잭션 관리가 필요한 부서 삽입 메서드
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertDept(HrmVO hrmVO) {
        return deptMapper.insertDept(hrmVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDept(HrmVO hrmVO) {
        return deptMapper.updateDept(hrmVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDept(Integer deptNo) {
        return deptMapper.deleteDept(deptNo);
    }
    @Override
    public boolean hasEmployeesInDept(Integer deptNo) {
        return deptMapper.countEmployeesInDept(deptNo) > 0;
    }

 // 부서별 사원 목록 조회
    @Override
    public List<HrmVO> getEmployeesByDept(Integer deptNo) {
        return deptMapper.selectEmployeesByDept(deptNo);
    }

    // 부서장 업데이트
    @Override
    @Transactional
    public int updateManager(Integer deptNo, Integer empNo) {
        return deptMapper.updateManager(deptNo, empNo);
    }

    @Override
    public List<HrmVO> getExistingDepts() {
        return deptMapper.selectDeptList();
    }
}
    
