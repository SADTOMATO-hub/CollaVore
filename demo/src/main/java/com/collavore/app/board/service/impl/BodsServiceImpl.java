package com.collavore.app.board.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.board.mapper.BodsMapper;
import com.collavore.app.board.service.BodsComtsVO;
import com.collavore.app.board.service.BodsService;
import com.collavore.app.board.service.BodsVO;

@Service
public class BodsServiceImpl implements BodsService {
	private BodsMapper bodsMapper;

	@Autowired
	BodsServiceImpl(BodsMapper bodsMapper) {
		this.bodsMapper = bodsMapper;
	}

	// 전체 게시글 수 확인하기.
	@Override
	public int totalListCnt(BodsVO bodsVO) {
		return bodsMapper.totalBoardCnt(bodsVO);
	}

	@Override
	public List<BodsVO> bodsList(BodsVO bodsVO) {
		return bodsMapper.selectBoardAll(bodsVO);
	}

	@Override
	public BodsVO bodsInfo(BodsVO bodsVO) {
		return bodsMapper.selectBodsInfo(bodsVO);
	}

	@Override
	public int insertBods(BodsVO bodsVO) {
		int result = bodsMapper.insertBodsInfo(bodsVO);
		return result == 1 ? bodsVO.getPostNo() : -1;
	}

	@Override
	public Map<String, Object> updateBods(BodsVO bodsVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;

		int result = bodsMapper.updateBodsInfo(bodsVO);
		if (result == 1) {
			isSuccessed = true;
		}

		String updateDate = getUpdateDate();

		map.put("date", updateDate);
		map.put("result", isSuccessed);
		map.put("target", bodsVO);

		return map;
	}

	private String getUpdateDate() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String updateDt = today.format(dtFormat);
		return updateDt;

		// return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
	}

	@Override
	public int deleteBods(int bodsVO) {
		return bodsMapper.deleteBodsInfo(bodsVO);
		//return 0;
	}

	@Override
	public int insertBodsComts(BodsComtsVO bodsComtsVO) {
		// TODO Auto-generated method stub
		return 0;
	}
}
