package com.collavore.app.cals.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.collavore.app.cals.mapper.CalsMapper;
import com.collavore.app.cals.service.CalsService;
import com.collavore.app.cals.service.CalsVO;

public class CalsServiceImpl implements CalsService {
	private final CalsMapper calsMapper;

	@Autowired // 생성자 주입
	public CalsServiceImpl(CalsMapper calsMapper) {
		this.calsMapper = calsMapper;
	}
	
	
	 @Override
	    public List<CalsVO> soloCal() {
	        return calsMapper.selectSoloCal();
	    }

	    @Override
	    public List<CalsVO> teamCal() {
	        return calsMapper.selectTeamCal();
	    }

	    @Override
	    public List<CalsVO> projCal() {
	        return calsMapper.selectProjCal();
	    }

	

}
