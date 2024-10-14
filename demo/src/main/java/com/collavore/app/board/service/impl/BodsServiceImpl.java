package com.collavore.app.board.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.board.mapper.BodsMapper;
import com.collavore.app.board.service.BodsService;
import com.collavore.app.board.service.BodsVO;

@Service
public class BodsServiceImpl implements BodsService {
	private BodsMapper bodsMapper;

	@Autowired
	BodsServiceImpl(BodsMapper bodsMapper) {
		this.bodsMapper = bodsMapper;
	}

	/*@Override
	public List<BodsVO> boardAll() {
		return bodsMapper.selectBoardAll();
	}// end boardList*/

	@Override
	public List<BodsVO> bodsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BodsVO bodsInfo(BodsVO bodsVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertBods(BodsVO bodsVO) {
		int result = bodsMapper.insertBodsInfo(bodsVO);
		return result == 1 ? bodsVO.getPost_no() : -1;
	}

	@Override
	public Map<String, Object> updateBods(BodsVO bodVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteBods(int post_no) {
		// TODO Auto-generated method stub
		return 0;
	}
}
