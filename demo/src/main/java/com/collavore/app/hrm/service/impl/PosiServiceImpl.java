package com.collavore.app.hrm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.hrm.mapper.PosiMapper;
import com.collavore.app.hrm.service.HrmVO;
import com.collavore.app.hrm.service.PosiService;

@Service
public class PosiServiceImpl implements PosiService {
	private PosiMapper posiMapper;

	@Autowired
	PosiServiceImpl(PosiMapper posiMapper) {
		this.posiMapper = posiMapper;
	}

	
	// 직위
	@Override
	public int posiInsert(HrmVO hrmVO) {
	    System.out.println("Inserting: " + hrmVO);  // 삽입할 데이터 출력
	    return posiMapper.insertPosiInfo(hrmVO);
	}

	@Override
	public int updatePosition(HrmVO hrmVO) {
	    System.out.println("Updating: " + hrmVO);  // 업데이트할 데이터 출력
	    return posiMapper.updatePosiInfo(hrmVO);
	}

	@Override
	public int deletePosition(Integer posiNo) {
		return posiMapper.deletePosiInfo(posiNo);
	}

	@Override
	public List<HrmVO> getExistingPositions() {
		return posiMapper.selectPosiList();
	}

}
