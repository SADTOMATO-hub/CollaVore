package com.collavore.app.hrm.service;

import java.util.List;

public interface DeptService {
    
    // 새로운 부서 추가
    public int insertDept(HrmVO hrmVO) throws Exception;
    
    // 기존 부서 정보 업데이트
    public int updateDept(HrmVO hrmVO) throws Exception;
    
    // 부서 삭제
    public int deleteDept(Integer deptNo) throws Exception;
    
    // 기존 부서 목록 가져오기
    public List<HrmVO> getExistingDepts();
}
