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
    public List<HrmVO> getExistingDepts() {
        return deptMapper.selectDeptList();
    }
}
