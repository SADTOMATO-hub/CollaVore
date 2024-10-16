package com.collavore.app.hrm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.hrm.mapper.DeptMapper;
import com.collavore.app.hrm.service.DeptService;
import com.collavore.app.hrm.service.HrmVO;

@Service
public class DeptServiceImpl implements DeptService {
	private DeptMapper deptMapper;

	@Autowired
	DeptServiceImpl(DeptMapper deptMapper) {
		this.deptMapper = deptMapper;
	}

	@Override
	public List<HrmVO> deptList() {
		return deptMapper.selectDeptAll();
	}

	@Override
	public HrmVO deptInfo(HrmVO hrmVO) {
		return deptMapper.selectDeptInfo(hrmVO);
	}

	@Override
	public int deptInsert(HrmVO hrmVO) {
		int result = deptMapper.insertDeptInfo(hrmVO);

		return result == 1 ? hrmVO.getDeptNo() : -1;
	}

	@Override
	public Map<String, Object> deptUpdate(HrmVO hrmVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;

		int result = deptMapper.updateDeptInfo(hrmVO.getDeptNo(), hrmVO);

		if (result == 1) {
			isSuccessed = true;
		}
		map.put("result", isSuccessed);
		map.put("target", hrmVO);
		return map;
	}

	@Override
	public Map<String, Object> deptDelete(int deptNo) {
		Map<String, Object> map = new HashMap<>();
		int result = deptMapper.deleteDeptInfo(deptNo);

		if (result == 1) {
			map.put("DepartmentId", deptNo);
		}
		return map;
	}

	// 직위
	@Override
	public int posiInsert(HrmVO hrmVO) {
	    System.out.println("Inserting: " + hrmVO);  // 삽입할 데이터 출력
	    return deptMapper.insertPosiInfo(hrmVO);
	}

	@Override
	public int updatePosition(HrmVO hrmVO) {
	    System.out.println("Updating: " + hrmVO);  // 업데이트할 데이터 출력
	    return deptMapper.updatePosiInfo(hrmVO);
	}

	@Override
	public int deletePosition(Integer posiNo) {
		return deptMapper.deletePosiInfo(posiNo);
	}

	@Override
	public List<HrmVO> getExistingPositions() {
		return deptMapper.selectPosiList();
	}

}
