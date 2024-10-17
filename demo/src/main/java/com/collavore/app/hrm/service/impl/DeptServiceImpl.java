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
}
