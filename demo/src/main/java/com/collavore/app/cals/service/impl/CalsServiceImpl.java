package com.collavore.app.cals.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.cals.mapper.CalsMapper;
import com.collavore.app.cals.service.CalsService;
import com.collavore.app.cals.service.CalsVO;

@Service
public class CalsServiceImpl implements CalsService{
	private CalsMapper calendarMapper;
	
	@Autowired // 생성자 주입
	public CalsServiceImpl(CalsMapper calendarMapper) {
		this.calendarMapper = calendarMapper;
	}
	@Override
	public List<CalsVO> calList(){
		return calendarMapper.selectCalAll();
	}
}
