package com.collavore.app.cals.mapper;

import java.util.List;

import com.collavore.app.cals.service.CalsVO;

public interface CalsMapper {
	// 전체조회
	public List<CalsVO> selectCalAll();
}
