package com.collavore.app.board.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.board.mapper.BodsMapper;
import com.collavore.app.board.service.BodsService;
import com.collavore.app.board.service.BodsVO;

@Service
public class BodsServiceImpl implements BodsService {
	private BodsMapper bodsMapper;
	
	@Autowired
	BodsServiceImpl(BodsMapper bodsMapper){
		this.bodsMapper = bodsMapper;
	}
	
	@Override
	public List<BodsVO> boardAll() {
		return bodsMapper.selectBoardAll();
	}// end boardList
}
